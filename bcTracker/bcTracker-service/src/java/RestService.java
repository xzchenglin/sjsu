import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import processor.BizGetProcessor;
import processor.BizPostProcessor;

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
                restConfiguration().component("restlet").host("localhost").port(2828).bindingMode(RestBindingMode.auto);

                rest("/rest").enableCORS(true)
                        .get("/{path}").to("direct:bizget")
                        .post("/{path}").to("direct:bizpost");

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