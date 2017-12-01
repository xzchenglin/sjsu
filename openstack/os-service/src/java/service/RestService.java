package service;

import service.processor.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

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
                restConfiguration().component("restlet").host("localhost").port(8283).bindingMode(RestBindingMode.auto);
                rest("/os")
                        .get("/list").to("direct:list")
                        .get("/del").to("direct:del")
                        .get("/create").to("direct:create")
                        .get("/op").to("direct:op");

                from("direct:list")
                        .process(new ListProcessor());
                from("direct:del")
                        .process(new DeleteProcessor());
                from("direct:create")
                        .process(new CreateProcessor());
                from("direct:op")
                        .process(new OpProcessor());
            }
        });
    }

    public static void main(String[] args){
        try {
            instance.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}