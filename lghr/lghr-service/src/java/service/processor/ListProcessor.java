package service.processor;

import common.JsonHelper;

/***
 *Created by Lin Cheng
 */
public class ListProcessor extends GetProcessor {
    @Override
    String handle() {

        switch (paramMap.get("type")){
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
