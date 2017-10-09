package camel.processor;

import camel.TwitterService;

/***
 *Created by Lin Cheng
 */
public class KeywordProcessor extends PostProcessor{
    @Override
    public String handle() {
        try {
            TwitterService.instance().setKeyword(body);
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
