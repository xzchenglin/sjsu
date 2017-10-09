package camel;

import common.Utils;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import twitter4j.DirectMessage;
import twitter4j.Status;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Lin Cheng on 2017/9/25.
 *
 This service will pull Twitter contents and put them to local file system.

 Supported twitter APIs and their destination folders:

 timeline -> /lghr/camel_d/timeline
 search -> /lghr/camel_d/search
 directmessage -> /lghr/camel_d/directmessage

 APIs exposed:

 TwitterService.start -> start service and keep pulling default contents
 TwitterService.stop -> stop service
 TwitterService.setKeyword -> change search keyword
 TwitterService.setTimeline -> change to different people's timeline
 */
public class TwitterService extends CamelService {

    private String keyword = "camel";
    private String timeline = "BarackObama";

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessTokenSecret;

    static private String searchRouteId = "searchRoute";
    static private String msgRouteId = "msgRoute";
    static private String timelineRouteId = "timelineRoute";

    private static String rootDir = "/lghr/camel_d/";

    private String searchUri = "search";
    private String directmessageUri = "directmessage";
    private String timelineUri = "timeline";

    static TwitterService instance = new TwitterService();

    private TwitterService() {
    }

    static public TwitterService instance(){
        return instance;
    }

    static public TwitterService instance(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){
        instance.accessToken = accessToken;
        instance.consumerKey = consumerKey;
        instance.consumerSecret = consumerSecret;
        instance.accessTokenSecret = accessTokenSecret;

        return instance;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) throws Exception {
        this.keyword = keyword;

        //enforce
        removeRoute(searchRouteId);
        addSearchRoute();
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) throws Exception {
        this.timeline = timeline;

        //enforce
        removeRoute(timelineRouteId);
        addTimelineRoute();
    }

    @Override
    public void addRoutes() throws Exception {

        addTimelineRoute();
        addSearchRoute();
        addMsgRoute();

        //add REST endpoints
        addRoute(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                restConfiguration().component("restlet").host("localhost").port(8000).bindingMode(RestBindingMode.auto);
                rest("/twitter")
                        .get("/timeline").to("direct:timeline")
                        .get("/msg").to("direct:msg")
                        .get("/search").to("direct:search")
                        .get("/testget").to("direct:testget")
                        .post("/testpost").to("direct:testpost");

                from("direct:timeline")
                        .bean(TwitterService.class, "readFile('timeline')").process(new PrintProcessor());
                from("direct:msg")
                        .bean(TwitterService.class, "readFile('directmessage')").process(new PrintProcessor());
                from("direct:search")
                        .bean(TwitterService.class, "readFile('search')").process(new PrintProcessor());

                from("direct:testget")
                        .process(new GetProcessor());
                from("direct:testpost")
                        .process(new PostProcessor());
            }
        });
    }

    public static String readFile(String filenName) {
        try {
            return Utils.readRawContentFromFile(rootDir + filenName + ".txt");
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private void addTimelineRoute() throws Exception{
        addTwitterRoute(timelineUri +
                "/user?type=direct&user=" + timeline, timelineRouteId);
    }

    private void addSearchRoute() throws Exception{
        addTwitterRoute(searchUri +
                "?type=polling&keywords=" + keyword, searchRouteId);
    }

    private void addMsgRoute() throws Exception{
        addTwitterRoute(directmessageUri +
                "?type=polling&delay=10000", msgRouteId);
    }

    private void addTwitterRoute(String uri, String id) throws Exception{
        addRoute(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("twitter://" + uri +
                        "&consumerKey=" + consumerKey +
                        "&consumerSecret=" + consumerSecret +
                        "&accessToken=" + accessToken +
                        "&accessTokenSecret=" + accessTokenSecret)
                        .routeId(id)
                        .process(new TwitterProcessor())
                        .process(new PrintProcessor())
                        .to("file:" + rootDir + "?fileExist=append&noop=true");
            }
        });
    }

    static class PrintProcessor implements Processor{
        @Override
        public void process(Exchange exchange) throws Exception {
            InputStream inputStream = exchange.getIn().getBody(InputStream.class);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line=bufferedReader.readLine())!=null){
                System.out.println(line);
            }
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class PostProcessor implements Processor{
        @Override
        public void process(Exchange exchange) throws Exception {
            InputStream inputStream = exchange.getIn().getBody(InputStream.class);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                String body = "";
                while ((line = bufferedReader.readLine()) != null) {
                    body += line + "\n";
                }

                //TODO twitter API calls and parsing

                exchange.getOut().setBody("post response");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inputStream.close();
            }
        }
    }

    static class GetProcessor implements Processor{
        @Override
        public void process(Exchange exchange) throws Exception {
            String params = exchange.getIn().getHeader("CamelHttpQuery") + "";

            //TODO twitter API calls and parsing

            exchange.getOut().setBody("get response");
        }
    }

    static class TwitterProcessor implements Processor {

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
}