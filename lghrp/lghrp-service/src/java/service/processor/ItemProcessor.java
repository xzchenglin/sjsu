package service.processor;

import common.JsonHelper;
import dao.BaseDao;
import dao.SchoolImpl;
import dao.UserImpl;
import dao.GroupImpl;

/***
 *Created by Lin Cheng
 */
public class ItemProcessor extends GetProcessor {
    @Override
    String handle() throws Exception {

        BaseDao dao;

        Long id = Long.parseLong(paramMap.get("id"));
        switch (paramMap.get("type")){
            case "user":
                dao = new UserImpl();
                return JsonHelper.toJson(dao.getById(id));
            case "group":
                dao = new GroupImpl();
                return JsonHelper.toJson(dao.getById(id));
            case "school":
                dao = new SchoolImpl();
                return JsonHelper.toJson(dao.getById(id));
            default:
                return JsonHelper.toJson("Not supported.");
        }
    }
}
