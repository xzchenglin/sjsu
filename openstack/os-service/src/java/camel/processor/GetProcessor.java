package camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/***
 *Created by Lin Cheng
 */
abstract class GetProcessor implements Processor {

    protected String params;

    @Override
    public void process(Exchange exchange) throws Exception {
        params = exchange.getIn().getHeader("CamelHttpQuery") + "";

        String resp = handle();
        exchange.getOut().setBody(resp);
    }

    abstract String handle();
}
