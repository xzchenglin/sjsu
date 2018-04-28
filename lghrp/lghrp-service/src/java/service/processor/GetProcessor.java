package service.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/***
 *Created by Lin Cheng
 */
abstract class GetProcessor implements Processor {

    protected Map<String, String> paramMap;

    @Override
    public void process(Exchange exchange) throws Exception {
        String params = exchange.getIn().getHeader("CamelHttpQuery") + "";
        paramMap = Arrays.stream(params.split("&")).map(s->s.split("=")).collect(Collectors.toMap(a->a[0], a->a[1]));

        String resp = handle();

        Map<String, Object> map = new HashMap<>();
        map.put( "Access-Control-Allow-Origin","*" );
        map.put( "Access-Control-Allow-Methods","*" );
        exchange.getOut().setHeaders(map);
        exchange.getOut().setBody(resp);
    }

    abstract String handle() throws Exception;
}
