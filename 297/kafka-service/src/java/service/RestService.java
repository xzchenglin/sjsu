package service;

import kafka.KafkaHelper;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import service.processor.ReadProcessor;
import service.processor.WriteProcessor;

/**
 * Created by Lin Cheng
 *
 */
public class RestService extends CamelService {

    static RestService instance = new RestService();

    private RestService() {
    }

    static public RestService instance(){
        return instance;
    }

    @Override
    public void addRoutes() throws Exception {
        //add REST endpoints
        addRoute(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                restConfiguration().component("restlet").host("localhost").port(8297).bindingMode(RestBindingMode.auto);
                rest("/kafka")
                        .get("/read").to("direct:read")
                        .post("/write").to("direct:write");

                from("direct:read")
                        .process(new ReadProcessor());
                from("direct:write")
                        .process(new WriteProcessor());
            }
        });
    }

    public static void main(String[] args){
        try {
            (new Thread(() -> {KafkaHelper.startListen("test1");})).start();
            (new Thread(() -> {KafkaHelper.startListen("test2");})).start();
            (new Thread(() -> {KafkaHelper.startListen("test3");})).start();

            instance.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}