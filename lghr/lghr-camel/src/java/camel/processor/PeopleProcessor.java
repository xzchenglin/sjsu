package camel.processor;

import camel.TwitterService;

/***
 *Created by Lin Cheng
 */
public class PeopleProcessor extends PostProcessor{
    @Override
    public String handle() {
        try {
            TwitterService.instance().setTimeline(body);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
