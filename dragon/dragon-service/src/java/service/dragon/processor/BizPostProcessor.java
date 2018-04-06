package service.dragon.processor;

import dragon.comm.JSONHelper;
import dragon.dao.Daos;
import dragon.dao.GroupBean;
import dragon.dao.GroupDao;
import dragon.model.food.Group;
import dragon.model.food.User;

/***
 *Created by Lin Cheng
 */
public class BizPostProcessor extends PostProcessor {
    @Override
    String handle() throws Exception {

        switch (path){
            case "init":
                GroupDao gb = Daos.get(GroupDao.class);
                Group g = JSONHelper.fromJson2(body, Group.class);
                g = gb.saveGroup(g);
                String mail = paramMap.get("mail");
                if(mail != null) {
                    gb.saveUser(new User(mail), false);
                    gb.saveUserToGroup(mail, g.getId(), true);
                }
                if (Boolean.parseBoolean(paramMap.get("apply"))) {
                    gb.applyPreference(g);
                }
                return JSONHelper.toJson(g);

            default:
                return JSONHelper.toJson("Not supported.");
        }

    }
}
