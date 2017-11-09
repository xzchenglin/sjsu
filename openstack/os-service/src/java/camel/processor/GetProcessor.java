package camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.Arrays;
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
        paramMap = Arrays.stream(params.split("&")).
                collect(Collectors.toMap(s->s.split("=")[0], s->s.split("=")[1]));

        String resp = handle();
        exchange.getOut().setBody(resp);
    }

    abstract String handle();
}
