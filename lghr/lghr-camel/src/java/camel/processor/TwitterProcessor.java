package camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import twitter4j.DirectMessage;
import twitter4j.Status;

import java.util.List;
import java.util.stream.Collectors;

/***
 *Created by Lin Cheng
 */
public class TwitterProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Object obj = exchange.getIn().getBody();
        if(obj == null){
            return;
        }

        String name = exchange.getFromEndpoint().getEndpointUri().toString().
                split("//")[1].split("[/?]")[0] + ".txt";

        String content;
        if(obj instanceof Status) {
            Status status = (Status)obj;
            content = status.getText();
        } else if(obj instanceof List) {
            List<Status> ss = (List<Status>)obj;
            content = ss.stream().map(o->o.getText()).collect(Collectors.joining("###"));
        } else if(obj instanceof DirectMessage){
            DirectMessage msg = (DirectMessage)obj;
            content = msg.getText();
        } else {
            content = obj + "";
        }

        exchange.getOut().setHeader(Exchange.FILE_NAME, name);
        exchange.getOut().setBody(content + "###");
    }
}
