package service;

import service.processor.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

/**
 * Created by Lin Cheng. and modified by Hyunwook Shin
 *
 *
 POST:localhost:8000/twitter/api -> five other apis depending on "action" field.
 Input data must be in JSON format:
 { "action": <action>,
 "credentials" : {
 "accessToken" : <access-token>,
 "accessTokenKey" : <access-token-key>
 }
 "data" : <data>
 }
 The POST request will return in JSON format:
 { "error" : <error>,
 "value" : <result> }
 action             description
 - search             get tweets from the user
 - nearby             get locations nearby GPS coordinates
 - favorites          get tweets liked by the user
 - timeline           get tweets on home timeline
 - account            get screen name
 - friends            get information (email,id,name) for each friend
 - followers          get information (email,id,name) for each follower
 - tweet              post a new tweet
 - retweet            retweet other user's tweet
 - unfollow           unfollow an user
 - follow             follow an user
 */
public class RestService extends CamelService {

    static RestService instance = new RestService();

    private RestService() {
    }

    static public RestService instance(){
        return instance;
    }

    @Override
    public void addRoutes() throws Exception {
        //add REST endpoints
        addRoute(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                restConfiguration().component("restlet").host("localhost").port(8272).bindingMode(RestBindingMode.auto);

                rest("/twitter").enableCORS(true)
                        .post("/api").to("direct:twitter"); /* hyunwook shin */

                rest("/rest").enableCORS(true)
                        .get("/list").to("direct:list")
                        .get("/item").to("direct:item")
                        .delete("/del").to("direct:del")
                        .post("/create").to("direct:create")
                        .post("/exlogin").to("direct:exlogin")
                        .post("/update").to("direct:update");

                from("direct:twitter")
                        .process(new ServerPostProcessor());

                from("direct:item")
                        .process(new ItemProcessor());
                from("direct:list")
                        .process(new ListProcessor());
                from("direct:del")
                        .process(new DeleteProcessor());
                from("direct:create")
                        .process(new CreateProcessor());
                from("direct:update")
                        .process(new UpdateProcessor());
                from("direct:exlogin")
                        .process(new ExloginProcessor());
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