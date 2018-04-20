package processor;

import helper.JSONHelper;

/***
 *Created by Lin Cheng
 */
public class BizPostProcessor extends PostProcessor {
    @Override
    String handle() throws Exception {

        switch (path){

            default:
                return JSONHelper.toJson("Not supported.");
        }

    }
}
