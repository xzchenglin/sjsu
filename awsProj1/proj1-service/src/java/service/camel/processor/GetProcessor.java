package service.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/***
 *Created by Lin Cheng
 */
abstract class GetProcessor implements Processor {

    protected Map<String, String> paraMap = new HashMap<>();

    @Override
    public void process(Exchange exchange) throws Exception {
        if(exchange.getIn().getHeader("CamelHttpQuery") != null){
            String[] parts = exchange.getIn().getHeader("CamelHttpQuery").toString().split("&");
            paraMap = Arrays.stream(parts).collect(Collectors.toMap(p->p.split("=")[0], p -> {
                try {
                    return URLDecoder.decode(p.split("=")[1], "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return p.split("=")[1];
                }
            }));
        } else {
            paraMap = new HashMap<>();
        }

        String resp = handle();
//        Set<String> head = new HashSet<>();
//        head.add("x");
//        Map map = new HashMap();
//        map.put("a", "b");
//        Response r = Response.getCurrent();
//        r.setAccessControlAllowHeaders(head);
//        r.setStatus(Status.CLIENT_ERROR_FAILED_DEPENDENCY);
//        r.setAttributes(map);

        setHeaders(exchange);
//        exchange.getOut().setBody(Response.getCurrent());
        exchange.getOut().setBody(resp);
    }

    protected void setHeaders(Exchange exchange){}
    abstract String handle() throws Exception;
}
