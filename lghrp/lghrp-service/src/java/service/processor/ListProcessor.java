package service.processor;

import common.JsonHelper;
import dao.*;

/***
 *Created by Lin Cheng
 */
public class ListProcessor extends GetProcessor {
    @Override
    String handle() throws Exception {

        BaseDao dao;

        switch (paramMap.get("type")){
            case "customer":
                dao = new UserImpl();
                return JsonHelper.toJson(dao.list(null));
            case "product":
                dao = new SchoolImpl();
                return JsonHelper.toJson(dao.list(paramMap.get("keyword")));
            case "order":
                dao = new GroupImpl();
                return JsonHelper.toJson(dao.list(paramMap.get("custId")));
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
