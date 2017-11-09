package camel.processor;

import camel.JsonHelper;
import vm.OpenstackController;

/***
 *Created by Lin Cheng
 */
public class ListProcessor extends GetProcessor {
    @Override
    String handle() {
        String[] ps = params.split("&");
        String type = "";
        for(String s:ps){
            String[] ss = s.split("=");
            if("type".equalsIgnoreCase(ss[0])){
                type = ss[1];
            }
        }

        switch (type){
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
