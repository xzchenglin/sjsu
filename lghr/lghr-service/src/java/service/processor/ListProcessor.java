package service.processor;

import common.JsonHelper;
import db.*;

/***
 *Created by Lin Cheng
 */
public class ListProcessor extends GetProcessor {
    @Override
    String handle() throws Exception {

        BaseDao dao;

        switch (paramMap.get("type")){
            case "customer":
                dao = new CustomerImpl();
                return JsonHelper.toJson(dao.list(null));
            case "product":
                dao = new ProductImpl();
                return JsonHelper.toJson(dao.list(paramMap.get("keyword")));
            case "order":
                dao = new OrderImpl();
                return JsonHelper.toJson(dao.list(paramMap.get("custId")));
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
