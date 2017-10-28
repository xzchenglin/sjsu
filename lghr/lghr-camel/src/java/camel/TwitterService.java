package camel;

import camel.processor.KeywordProcessor;
import camel.processor.PeopleProcessor;
import camel.processor.PrintProcessor;
import camel.processor.TwitterProcessor;
import common.Utils;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import java.io.File;

/**
 * Created by Lin Cheng on 2017/9/25.
 *
 This service will pull Twitter contents and put them to local file system.

 Supported twitter APIs and their destination folders:

 timeline -> /lghr/camel_d/timeline.txt
 search -> /lghr/camel_d/search.txt
 directmessage -> /lghr/camel_d/directmessage.txt

 REST APIs:

 GET:localhost:8000/twitter/timeline -> get twitters from current timeline
 GET:localhost:8000/twitter/msg -> get message (current user only)
 GET:localhost:8000/twitter/search -> get search results using the current keyword
 POST:localhost:8000/twitter/keyword -> set new search keyword
 POST:localhost:8000/twitter/people -> set new people's timeline
 */
public class TwitterService extends CamelService {

    private String keyword = "obama";
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

    static TwitterService instance = new TwitterService("A3r6vuJNqxRxWy7vvQYKvN3zY",
            "FoZQyC6zyrrRb7FUFbei3q0gXOCHISbExU0UADHWIFDUFol3c4",
            "42130867-xhH261CGI62un52HGm66Iks2Xlw7OYaJijm3ucwVG",
            "0TN0VWs6CFsYKFDHxKtZnmbIgaLMuIxI8Itl2vsHWyjwl");

    private TwitterService() {
    }

    public TwitterService(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessTokenSecret = accessTokenSecret;
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
        new File(rootDir + searchUri + ".txt").delete();
        removeRoute(searchRouteId);
        addSearchRoute();
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) throws Exception {
        this.timeline = timeline;

        //enforce
        new File(rootDir + timelineUri + ".txt").delete();
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
                        .post("/keyword").to("direct:keyword")
                        .post("/people").to("direct:people");
//                        .get("/testget").to("direct:testget")
//                        .post("/testpost").to("direct:testpost");

                from("direct:timeline")
                        .bean(TwitterService.class, "readFile('timeline')").process(new PrintProcessor());
                from("direct:msg")
                        .bean(TwitterService.class, "readFile('directmessage')").process(new PrintProcessor());
                from("direct:search")
                        .bean(TwitterService.class, "readFile('search')").process(new PrintProcessor());
                from("direct:keyword")
                        .process(new KeywordProcessor());
                from("direct:people")
                        .process(new PeopleProcessor());

//                from("direct:testget")
//                        .process(new GetProcessor());
//                from("direct:testpost")
//                        .process(new PostProcessor());
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
                "?type=polling&count=50&keywords=" + keyword, searchRouteId);
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

    public static void main(String[] args){
        try {
            instance.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}