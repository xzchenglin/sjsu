package service.dragon.processor;

import dragon.comm.JSONHelper;
import dragon.dao.Daos;
import dragon.dao.GroupDao;
import dragon.model.food.Group;
import dragon.model.food.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/***
 *Created by Lin Cheng
 */
public class BizPostProcessor extends PostProcessor {

    static Log logger = LogFactory.getLog(BizPostProcessor.class);

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
                try {
                    if (Boolean.parseBoolean(paramMap.get("apply"))) {
                        gb.applyPreference(g);
                    }
                } catch (Exception e){
                    logger.error("Failed to apply setting: " + e.getMessage());
                }
                return JSONHelper.toJson(g);

            default:
                return JSONHelper.toJson("Not supported.");
        }

    }
}
