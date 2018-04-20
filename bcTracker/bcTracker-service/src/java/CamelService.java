import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Created by Lin Cheng
 */
public abstract class CamelService {

    private volatile boolean stop = false;
    private CamelContext camelContext = new DefaultCamelContext();

    public void start() throws Exception {
        addRoutes();
        camelContext.start();

        while (!stop) {
            Thread.sleep(1000);
        }

        try {
            camelContext.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        stop = true;
    }

    protected void addRoute(RouteBuilder r) throws Exception {
        camelContext.addRoutes(r);
    }

    protected void removeRoute(String rid) throws Exception {
        camelContext.removeRoute(rid);
    }

    abstract void addRoutes() throws Exception;

 }