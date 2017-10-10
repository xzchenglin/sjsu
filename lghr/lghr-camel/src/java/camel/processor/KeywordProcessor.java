package camel.processor;

import camel.TwitterService;

/***
 *Created by Lin Cheng
 */
public class KeywordProcessor extends PostProcessor{
    @Override
    public String handle() throws Exception {
        TwitterService.instance().setKeyword(body);
        return "OK";
    }
}
