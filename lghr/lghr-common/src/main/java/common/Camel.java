package main.java.common;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Created by Lin on 2017/9/17.
 */
public class Camel {

    private static volatile boolean stop = false;
    private static CamelContext camelContext = new DefaultCamelContext();
    private static Camel camel = new Camel();

    private Camel() {
    }

    public static Camel instance(){
        return camel;
    }

    public void start() throws Exception {
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

    public void addRoute(RouteBuilder r) throws Exception {
        camelContext.addRoutes(r);
    }

    public void removeRoute(String rid) throws Exception {
        camelContext.removeRoute(rid);
    }

 }