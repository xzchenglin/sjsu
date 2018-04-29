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
        UserDao dao = new UserImpl();
        return JsonHelper.toJson(dao.createAndGetByExId(JsonHelper.fromJson2(body, User.class)));
    }
}
