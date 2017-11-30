package service.helper;

import org.json.JSONObject;
import twitter4j.Place;
import twitter4j.Status;
import twitter4j.User;

import java.util.List;
/***
 *Created by Hyunwook Shin
 */

public class JsonHelper {
    public String placesToJson(List<Place> places) {
        JSONObject info = new JSONObject();
        JSONObject placesInfo = new JSONObject();
        for (int i = 0; i < places.size(); i++) {
            JSONObject placeInfo = new JSONObject();
            placeInfo.put("country", places.get(i).getCountry());
            placeInfo.put("name", places.get(i).getName());
            placeInfo.put("type", places.get(i).getPlaceType());
            placesInfo.put(String.valueOf(i), placeInfo);
        }
        info.put("error", "");
        info.put("value", placesInfo);
        return info.toString();
    }

    public String errorToJson(String error) {
        JSONObject info = new JSONObject();
        info.put("error", error);
        info.put("value", "");
        return info.toString();
    }

    public String stringToJson(String x) {
        JSONObject info = new JSONObject();
        info.put("value", x);
        info.put("error", "");
        return info.toString();
    }

    public String usersToJson(List<User> users) {
        JSONObject info = new JSONObject();
        JSONObject usersInfo = new JSONObject();
        for (int i = 0; i < users.size(); i++) {
            JSONObject userinfo = new JSONObject();
            userinfo.put("email", users.get(i).getEmail());
            userinfo.put("id", users.get(i).getId());
            userinfo.put("location", users.get(i).getLocation());
            userinfo.put("profilePic", users.get(i).getBiggerProfileImageURL());
            usersInfo.put(users.get(i).getScreenName(), userinfo);
        }
        info.put("error", "");
        info.put("value", usersInfo);
        return info.toString();
    }

    public String statusToJson(Status status) {
        JSONObject info = new JSONObject();
        JSONObject tweetInfo = new JSONObject();
        tweetInfo.put("id", status.getId());
        tweetInfo.put("text", status.getText());
        tweetInfo.put(status.getUser().getScreenName(), tweetInfo);
        info.put("error", "");
        info.put("value", tweetInfo);
        return info.toString();
    }

    public String statusesToJson(List<Status> statuses) {
        JSONObject info = new JSONObject();
        JSONObject tweetsInfo = new JSONObject();
        for (Status status : statuses) {
            JSONObject tweetInfo = new JSONObject();
            tweetInfo.put("id", status.getId());
            tweetInfo.put("text", status.getText());
            tweetsInfo.put(status.getUser().getScreenName(), tweetInfo);
        }
        info.put("error", "");
        info.put("value", tweetsInfo);
        return info.toString();
    }
}