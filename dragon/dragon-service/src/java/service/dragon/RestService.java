package service.dragon;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.log4j.PropertyConfigurator;
import service.dragon.processor.BizGetProcessor;
import service.dragon.processor.BizPostProcessor;

import java.io.File;

/**
 * Created by Lin Cheng
 *
 */
public class RestService extends CamelService {

    static RestService instance = new RestService();

    private RestService() {
        String log4jConfigFile = "/opt/dragon/log4j.properties";
        if (new File(log4jConfigFile).exists()) {
            PropertyConfigurator.configureAndWatch(log4jConfigFile, 10000L);
        }

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
                restConfiguration().component("restlet").host("localhost").port(2772).bindingMode(RestBindingMode.auto);

                rest("/rest").enableCORS(true)
                        .get("/biz/{path}").to("direct:bizget")
                        .post("/biz/{path}").to("direct:bizpost");

                from("direct:bizget")
                        .process(new BizGetProcessor());
                from("direct:bizpost")
                        .process(new BizPostProcessor());
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