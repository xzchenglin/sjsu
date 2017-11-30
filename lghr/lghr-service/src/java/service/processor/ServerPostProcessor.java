package service.processor;


import org.json.JSONObject;
import service.helper.JsonHelper;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

/***
 *Created by Hyunwook Shin
 */
public class ServerPostProcessor extends  PostProcessor{

    Twitter twitterHandle( String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret ) {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumerKey);
        builder.setOAuthConsumerSecret(consumerSecret);
        builder.setOAuthAccessToken(accessToken);
        builder.setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory factory = new TwitterFactory(builder.build());
        return factory.getInstance();
    }

    @Override
    String handle() throws Exception {
        /* Takes json object and parses out
           [url], [method], and [data] portion
           and returns response to HTTP [method]
           request to [ur] with [data] as payload
         */
        String consumerKey = "lgjwlqVncQb8ZAy1EjdDRPIMJ";
        String consumerSecret = "lzV68NbXKe6wK3vqhjmJOGxm6koGlxOD0mkUbSF8bdU4W5jsi6";
        String accessToken;
        String accessTokenSecret;
        JsonHelper helper = new JsonHelper();
        JSONObject info = new JSONObject(body);
        JSONObject data = info.getJSONObject("data");

        String action = info.getString("action");
        String response;
        String name; // username
        System.out.println( body );
        System.out.println( "action is " + action );
        System.out.println( "data is " + data.toString() );

        /* Get the credentials from "credentials" */
        try {
            JSONObject credentials = info.getJSONObject( "credentials" );
            accessToken = credentials.getString( "accessToken" );
            accessTokenSecret = credentials.getString( "accessTokenSecret");
        } catch (Exception e) {
            return helper.errorToJson( "Invalid authentication format or invalid keys: " + e.toString() );
        }

        Twitter twitter = twitterHandle( consumerKey, consumerSecret, accessToken, accessTokenSecret );

        body = "";
        double SanJoseLatitude = 37.3382;
        double SanJoseLongitude = -121.8863;
        double latitude, longitude;
        try {
            switch (action) {
                case "nearby":
                    try {
                        latitude = data.getDouble("latitude");
                        longitude = data.getDouble("longitude");
                    } catch (Exception e){
                        latitude = SanJoseLatitude;
                        longitude = SanJoseLongitude;
                    }
                    GeoLocation location = new GeoLocation(latitude, longitude);
                    GeoQuery query = new GeoQuery(location);
                    response = helper.placesToJson( twitter.searchPlaces( query ) );
                    break;
                case "tweet":
                    twitter.updateStatus( data.getString( "status" ));
                    response = helper.stringToJson( "updated" );
                    break;
                case "retweet":
                    twitter.retweetStatus( data.getLong( "id"));
                    response = helper.stringToJson("retweeted");
                    break;
                case "search":
                    Query tweetQuery = new Query( data.getString( "keyword" ) );
                    response = helper.statusesToJson( twitter.search( tweetQuery ).getTweets());
                    break;
                case "favorites":
                    response = helper.statusesToJson( twitter.getFavorites() );
                    break;
                case "timeline":
                    response = helper.statusesToJson( twitter.getHomeTimeline() );
                    break;
                case "name":
                    response = helper.stringToJson( twitter.getAccountSettings().getScreenName());
                    break;
                case "follow":
                    twitter.createFriendship(data.getString( "user" ));
                    response = helper.stringToJson("now following");
                    break;
                case "unfollow":
                    twitter.destroyFriendship(data.getString( "user" ));
                    response = helper.stringToJson("unfollowing");
                    break;
                case "friends":
                    name = twitter.getAccountSettings().getScreenName();
                    response = helper.usersToJson(twitter.getFriendsList( name, -1 ));
                    break;
                case "followers":
                    name = twitter.getAccountSettings().getScreenName();
                    response = helper.usersToJson(twitter.getFollowersList( name, -1 ));
                    break;
                default:
                    response = helper.errorToJson("improper action");
            }
        } catch (Exception e) {
            response = helper.errorToJson( e.toString() );
        }
        return response;
    }
}