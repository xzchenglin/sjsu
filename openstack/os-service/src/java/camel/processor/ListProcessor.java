package camel.processor;

import camel.JsonHelper;
import vm.OpenstackController;

/***
 *Created by Lin Cheng
 */
public class ListProcessor extends GetProcessor {
    @Override
    String handle() {

        switch (paramMap.get("type")){
            case "flavor":
                return JsonHelper.toJson(OpenstackController.instance().getFlavors());
            case "server":
                return JsonHelper.toJson(OpenstackController.instance().getServers());
            case "image":
                return JsonHelper.toJson(OpenstackController.instance().getImages());
            case "network":
                return JsonHelper.toJson(OpenstackController.instance().getNetworks());
            default:
                return JsonHelper.toJson("Not suppported.");
        }
    }
}
