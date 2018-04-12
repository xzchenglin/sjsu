package service.dragon.processor;

import dragon.comm.JSONHelper;
import dragon.comm.Pair;
import dragon.dao.*;
import dragon.model.food.Group;
import dragon.model.food.Restaurant;
import dragon.model.food.Stat;
import dragon.model.food.Vote;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 *Created by Lin Cheng
 */
public class BizGetProcessor extends GetProcessor {

    @Override
    String handle() throws Exception {

        BizDao dao = Daos.get(BizDao.class);
        GroupDao gb = Daos.get(GroupDao.class);

        switch (path){

            case "user":
                List<Group> list = gb.getGroups(paramMap.get("mail"));
                return JSONHelper.toJson(list);

            case "pick":
                Restaurant r = dao.pickRestaurant(Long.parseLong(paramMap.get("gid")));
                return JSONHelper.toJson(r);

            case "vote":
                Vote.Result res = Vote.Result.values()[Integer.parseInt(paramMap.get("vote"))];
                Vote v = new Vote();
                v.setRecId(Long.parseLong(paramMap.get("id")));
                v.setResult(res);
                r = dao.vote(v, (Boolean.parseBoolean(paramMap.get("mute"))));
                return JSONHelper.toJson(r);

            case "summary":
                Map<String, Stat> ss = dao.stat(Long.parseLong(paramMap.get("gid")), 0 , true);
                return JSONHelper.toJson(ss);

            case "group":
                Group g = gb.getGroup(new Pair<>("name", paramMap.get("key")));
                gb.loadDependencies(g, 100);
                return JSONHelper.toJson(g);

            case "restaurant":
                r = dao.getRestaurant(new Pair<>("name", paramMap.get("key")));
                return JSONHelper.toJson(r);

            case "restaurants":
                Long gid = Long.parseLong(paramMap.get("gid"));
                List<Restaurant> rs = new ArrayList<Restaurant>();
                if (gid != null && gid > 0) {
                    rs = dao.getRestaurants(gid);
                } else {
                    rs = dao.getRestaurants("");
                }
                return JSONHelper.toJson(rs);

            default:
                return JSONHelper.toJson("Not supported.");
        }

    }
}
