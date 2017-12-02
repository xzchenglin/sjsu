package service.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/***
 *Created by Lin Cheng, modified by Hyunwook Shin
 */
abstract class PostProcessor implements Processor {

    protected String body = "";
    protected Map<String, String> paramMap;

    @Override
    public void process(Exchange exchange) throws Exception {
        InputStream inputStream = exchange.getIn().getBody(InputStream.class);
        try {
            body = "";

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                body += line;
            }
            body = body.trim();

            String params = exchange.getIn().getHeader("CamelHttpQuery") + "";
            paramMap = Arrays.stream(params.split("&")).map(s->s.split("=")).collect(Collectors.toMap(a->a[0], a->a[1]));

            String resp = handle();

            exchange.getOut().setBody(resp);
            Map<String, Object> map = new HashMap();
            map.put( "Access-Control-Allow-Origin","*" );
            map.put( "Access-Control-Allow-Methods","*" );
            exchange.getOut().setHeaders(map);

        } catch (Exception e) {
            e.printStackTrace();
            exchange.getOut().setBody(e.getMessage());
        } finally {
            inputStream.close();
        }
    }

    abstract String handle() throws Exception;
}

