package service.processor;

import common.JsonHelper;
import dao.BaseDao;
import dao.UserImpl;
import dao.GroupImpl;
import dao.SchoolImpl;
import model.User;
import model.Group;
import model.School;

/***
 *Created by Lin Cheng
 */
public class CreateProcessor extends PostProcessor {
    @Override
    String handle()throws Exception  {

        BaseDao dao;

        switch (paramMap.get("type")){
            case "product":
                dao = new SchoolImpl();
                return JsonHelper.toJson(dao.create(JsonHelper.fromJson(body, School.class)));
            case "order":
                dao = new GroupImpl();
                return JsonHelper.toJson(dao.create(JsonHelper.fromJson(body, Group.class)));
            case "customer":
                dao = new UserImpl();
                return JsonHelper.toJson(dao.create(JsonHelper.fromJson(body, User.class)));
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
