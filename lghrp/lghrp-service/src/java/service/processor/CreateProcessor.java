package service.processor;

import common.JsonHelper;
import dao.BaseDao;
import dao.UserImpl;
import dao.GroupImpl;
import dao.SchoolImpl;
import helper.DynamoHelper;
import javafx.geometry.Pos;
import model.Post;
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
            case "school":
                dao = new SchoolImpl();
                return JsonHelper.toJson(dao.create(JsonHelper.fromJson(body, School.class)));
            case "group":
                dao = new GroupImpl();
                return JsonHelper.toJson(dao.create(JsonHelper.fromJson(body, Group.class)));
            case "user":
                dao = new UserImpl();
                return JsonHelper.toJson(dao.create(JsonHelper.fromJson(body, User.class)));
            case "post":
                Post p = JsonHelper.fromJson2(body, Post.class);
                p.setTime(System.currentTimeMillis());
                DynamoHelper.write(p);
                return body;
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
