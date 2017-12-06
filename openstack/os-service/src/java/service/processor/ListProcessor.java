package service.processor;

import service.JsonHelper;
import vm.OpenstackController;

/***
 *Created by Lin Cheng
 */
public class ListProcessor extends UrlProcessor {
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
            case "subnet":
                return JsonHelper.toJson(OpenstackController.instance().getSubnets());
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
