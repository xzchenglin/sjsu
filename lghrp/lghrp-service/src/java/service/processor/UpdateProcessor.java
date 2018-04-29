package service.processor;

import common.JsonHelper;
import dao.BaseDao;
import dao.UserImpl;
import dao.GroupImpl;
import dao.SchoolImpl;
import model.Group;
import model.School;
import model.User;
import model.UserGroup;

/***
 *Created by Lin Cheng
 */
public class UpdateProcessor extends PostProcessor {
    @Override
    String handle() throws Exception {
        BaseDao dao;

        switch (paramMap.get("type")){
            case "school":
                dao = new SchoolImpl();
                return JsonHelper.toJson(dao.update(JsonHelper.fromJson(body, School.class)));
            case "group":
                dao = new GroupImpl();
                return JsonHelper.toJson(dao.update(JsonHelper.fromJson(body, Group.class)));
            case "user":
                dao = new UserImpl();
                return JsonHelper.toJson(dao.update(JsonHelper.fromJson(body, User.class)));
            case "userGroup":
                dao = new UserImpl();
                return JsonHelper.toJson(((UserImpl) dao).joinGroup(JsonHelper.fromJson2(body, UserGroup.class)));
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
