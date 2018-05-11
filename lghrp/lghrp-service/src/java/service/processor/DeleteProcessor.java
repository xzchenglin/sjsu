package service.processor;

import common.JsonHelper;
import dao.*;
import model.UserGroup;

/***
 *Created by Lin Cheng
 */
public class DeleteProcessor extends PostProcessor {
    @Override
    String handle() throws Exception {

        BaseDao dao;

        switch (paramMap.get("type")){
            case "userGroup":
                dao = new UserImpl();
                UserGroup ret = ((UserImpl) dao).exitGroup(JsonHelper.fromJson2(body, UserGroup.class));
                return JsonHelper.toJson(ret);
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
