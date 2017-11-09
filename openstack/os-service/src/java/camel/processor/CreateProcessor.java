package camel.processor;

import camel.JsonHelper;
import vm.OpenstackController;

/***
 *Created by Lin Cheng
 */
public class CreateProcessor extends GetProcessor {
    @Override
    String handle() {
        String[] ps = params.split("&");
        String type = "";
        String name = "";
        String flavor = "";
        String network = "";
        String image = "";
        for(String s:ps){
            String[] ss = s.split("=");
            if("type".equalsIgnoreCase(ss[0])){
                type = ss[1];
            }
            if("name".equalsIgnoreCase(ss[0])){
                name = ss[1];
            }
            if("flavor".equalsIgnoreCase(ss[0])){
                flavor = ss[1];
            }
            if("network".equalsIgnoreCase(ss[0])){
                network = ss[1];
            }
            if("image".equalsIgnoreCase(ss[0])){
                image = ss[1];
            }
        }

        switch (type){
            case "server":
                return JsonHelper.toJson(OpenstackController.instance().createServer(name, flavor, image, network));
            default:
                return JsonHelper.toJson("Not suppported.");
        }
    }
}
