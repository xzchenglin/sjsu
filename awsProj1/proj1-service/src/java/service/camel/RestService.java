package service.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import service.camel.processor.*;

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
                restConfiguration().component("restlet").host("localhost").port(8001).bindingMode(RestBindingMode.auto);
                rest("/s3")
                        .get("/list").produces("application/json").to("direct:list")
                        .get("/download").to("direct:download")
                        .get("/del").to("direct:del")
                        .get("/upload").to("direct:upload");

                from("direct:list")
                        .process(new ListProcessor());
                from("direct:download")
                        .process(new DownloadProcessor());
                from("direct:del")
                        .process(new DelProcessor());
                from("direct:upload")
                        .process(new UploadProcessor());
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