package service.processor;

import common.JsonHelper;
import dao.*;
import model.User;

/***
 *Created by Lin Cheng
 */
public class ExloginProcessor extends PostProcessor {

    @Override
    String handle() throws Exception {

        switch (paramMap.get("type")){
            case "customer":
                UserDao dao = new UserImpl();
                return JsonHelper.toJson(dao.createAndGetByExId(JsonHelper.fromJson(body, User.class)));
            default:
                return JsonHelper.toJson("Not supported.");
        }

    }
}
