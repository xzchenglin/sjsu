package service.processor;

import common.JsonHelper;
import dao.*;
import helper.DynamoHelper;
import model.Post;

import java.util.List;

/***
 *Created by Lin Cheng
 */
public class ListProcessor extends GetProcessor {
    @Override
    String handle() throws Exception {

        BaseDao dao;

        switch (paramMap.get("type")){
            case "user":
                dao = new UserImpl();
                return JsonHelper.toJson(dao.list(null));
            case "school":
                dao = new SchoolImpl();
                return JsonHelper.toJson(dao.list(paramMap.get("keyword")));
            case "group":
                dao = new GroupImpl();
                return JsonHelper.toJson(dao.list(paramMap.get("sid")));
            case "post":
                List<Post> ps = DynamoHelper.search(Long.parseLong(paramMap.get("gid")));
                return JsonHelper.toJson(ps);
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
