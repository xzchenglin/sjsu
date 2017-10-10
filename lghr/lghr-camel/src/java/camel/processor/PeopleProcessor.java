package camel.processor;

import camel.TwitterService;

/***
 *Created by Lin Cheng
 */
public class PeopleProcessor extends PostProcessor{
    @Override
    public String handle() throws Exception {
        TwitterService.instance().setTimeline(body);
        return "OK";
    }
}
