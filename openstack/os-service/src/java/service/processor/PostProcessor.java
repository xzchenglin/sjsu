package service.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/***
 *Created by Lin Cheng
 */
abstract class PostProcessor implements Processor {

    protected String body = "";

    @Override
    public void process(Exchange exchange) throws Exception {
        InputStream inputStream = exchange.getIn().getBody(InputStream.class);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                body += line;
            }
            body = body.trim();

            String resp = handle();

            Map<String, Object> map = new HashMap<>();
            map.put( "Access-Control-Allow-Origin","*" );
            map.put( "Access-Control-Allow-Methods","*" );
            exchange.getOut().setHeaders(map);
            exchange.getOut().setBody(resp);

        } catch (Exception e) {
            e.printStackTrace();
            exchange.getOut().setBody(e.getMessage());
        } finally {
            inputStream.close();
        }
    }

    abstract String handle() throws Exception;
}


