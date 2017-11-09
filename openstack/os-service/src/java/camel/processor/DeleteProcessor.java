package camel.processor;

import camel.JsonHelper;
import vm.OpenstackController;

/***
 *Created by Lin Cheng
 */
public class DeleteProcessor extends GetProcessor {
    @Override
    String handle() {

        switch (paramMap.get("type")){
            case "server":
                return JsonHelper.toJson(OpenstackController.instance().delServer(paramMap.get("id")));
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
