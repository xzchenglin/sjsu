package camel.processor;

import camel.JsonHelper;
import vm.OpenstackController;

/***
 *Created by Lin Cheng
 */
public class OpProcessor extends GetProcessor {
    @Override
    String handle() {
        switch (paramMap.get("type")){
            case "server":
                return JsonHelper.toJson(OpenstackController.instance().opServer(paramMap.get("id"), paramMap.get("action")));
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
