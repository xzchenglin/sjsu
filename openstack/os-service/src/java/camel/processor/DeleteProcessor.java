package camel.processor;

import camel.JsonHelper;
import vm.OpenstackController;

/***
 *Created by Lin Cheng
 */
public class DeleteProcessor extends GetProcessor {
    @Override
    String handle() {
        String[] ps = params.split("&");
        String type = "";
        String id = "";
        for(String s:ps){
            String[] ss = s.split("=");
            if("type".equalsIgnoreCase(ss[0])){
                type = ss[1];
            }
            if("id".equalsIgnoreCase(ss[0])){
                id = ss[1];
            }
        }

        switch (type){
            case "server":
                return JsonHelper.toJson(OpenstackController.instance().delServer(id));
            default:
                return JsonHelper.toJson("Not suppported.");
        }
    }
}
