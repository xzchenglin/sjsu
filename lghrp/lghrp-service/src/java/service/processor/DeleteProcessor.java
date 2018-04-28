package service.processor;

import common.JsonHelper;
import dao.*;

/***
 *Created by Lin Cheng
 */
public class DeleteProcessor extends GetProcessor {
    @Override
    String handle() throws Exception {

        BaseDao dao;

        switch (paramMap.get("type")){
            case "product":
                dao = new SchoolImpl();
                dao.deleteById(paramMap.get("id"));
                return JsonHelper.toJson("true");
            case "order":
                dao = new GroupImpl();
                dao.deleteById(paramMap.get("id"));
                return JsonHelper.toJson("true");
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
