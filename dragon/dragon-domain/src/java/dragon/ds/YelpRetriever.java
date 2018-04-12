package dragon.ds;

import dragon.dao.*;
import dragon.comm.ConfigHelper;
import dragon.comm.Pair;
import dragon.helper.DbHelper;
import dragon.model.food.Group;
import dragon.model.food.Restaurant;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin.cheng
 */
public class YelpRetriever implements DsRetriever {

    static GroupDao gb = Daos.get(GroupDao.class);
    static BizDao eb = Daos.get(BizDao.class);
    static Log logger = LogFactory.getLog(YelpRetriever.class);

    public String location = "";//must set by client apps
    public String category = "";//all if not set
    public String exclude = "";
    public String prefer = "";
    public String nopre = "";
    public String distance = ConfigHelper.instance().getConfig("distance");
    public String reviews = ConfigHelper.instance().getConfig("reviews");

    public YelpRetriever() {
    }

    public YelpRetriever(String settings) {
        applySettings(settings);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public String getPrefer() {
        return prefer;
    }

    public void setPrefer(String prefer) {
        this.prefer = prefer;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getNopre() {
        return nopre;
    }

    public void setNopre(String nopre) {
        this.nopre = nopre;
    }

    public List<Restaurant> searchAndImport(Long gid) throws Exception{

        List<Restaurant> ret = new ArrayList<Restaurant>();

        if(gid != null){
            Group group = gb.getGroup(new Pair<String, Object>("id", gid));
            if(group == null){
                logger.error("Group not found: " + gid);
                return ret;
            }
            applySettings(group.getPreference());
        }

        String[] exs = {};
        String[] pres = {};
        String[] nps = {};

        YelpAPI ya = new YelpAPI();
        YelpAPI.YelpAPICLI yaCli = getYaCli();
        if (StringUtils.isNotBlank(exclude)) {
            exs = exclude.split(",");
        }
        if (StringUtils.isNotBlank(prefer)) {
            pres = prefer.split(",");
        }
        if (StringUtils.isNotBlank(nopre)) {
            nps = nopre.split(",");
        }

        int cnt = 0;

        for (int i = 0; i < 3; i++) {//max = 200
            yaCli.offset = 20 * i;
            String json = YelpAPI.queryAPI(ya, yaCli);

            JSONParser parser = new JSONParser();
            JSONObject response = null;
            try {
                response = (JSONObject) parser.parse(json);
            } catch (ParseException pe) {
                logger.error("Error: could not parse JSON response:" + json);
                return ret;
            }

            JSONArray businesses = (JSONArray) response.get("businesses");
            logger.info(String.format("%s businesses found ...", businesses==null ? 0 : businesses.size()));
            if (businesses == null || businesses.size() == 0) {
                break;//no more
            }

            Connection conn = null;

            try {
                conn = DbHelper.getConn();
                for (Object obj : businesses) {
                    JSONObject bo = (JSONObject) obj;

                    String cats = bo.get("categories").toString().toLowerCase();
                    String name = bo.get("name").toString();
                    String id = bo.get("id").toString();
                    Integer rc = Integer.parseInt(bo.get("review_count").toString());

                    boolean excluded = rc < Integer.parseInt(reviews);
                    double adjust = 0;
                    if(!excluded) {
                        for (String ex : exs) {
                            if (cats.contains(ex) || id.contains(ex)) {
                                excluded = true;
                                break;
                            }
                        }
                    }

                    if (excluded) {
                        logger.info(id + " excluded.");
                        continue;
                    }

                    for (String pre : pres) {
                        if (cats.contains(pre) || id.contains(pre)) {
                            logger.info(id + " preferred.");
                            adjust += 0.5;
                            break;
                        }
                    }
                    for (String np : nps) {
                        if (cats.contains(np) || id.contains(np)) {
                            logger.info(id + " hated.");
                            adjust -= 1;
                            break;
                        }
                    }

                    Long factor = Math.round(Math.pow(2, Float.parseFloat(bo.get("rating").toString()))); // 2^rating
                    Long factorForGrp = Math.round(Math.pow(2, adjust + Float.parseFloat(bo.get("rating").toString())));
                    if(factor > 30){
                        factor = 30L;
                    }
                    if(factorForGrp > 30){
                        factorForGrp = 30L;
                    }

                    String cat = cats.split(",")[0];
                    cat = cat.substring(cat.indexOf("\"") + 1, cat.lastIndexOf("\""));

                    Restaurant r = new Restaurant(id, bo.get("url").toString(), factor, name, cat);
                    r.setSource("y");
                    Long rid = eb.saveRestaurant(r, conn);
                    r.setId(rid);

                    if(gid != null && gid > 0) {
                        gb.saveRestaurantToGroup(rid, gid, factorForGrp);
                    }

                    cnt++;
                    ret.add(r);
                }
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            } finally {
                DbHelper.closeConn(conn);
            }
        }

        logger.info(String.format("Total : %s businesses found.", cnt));
        return ret;
    }

    @Override
    public boolean checkOpen(String bid) throws Exception {
        YelpAPI ya = new YelpAPI();
        String json = ya.searchByBusinessId(bid);
        JSONParser parser = new JSONParser();
        JSONObject bo = null;
        try {
            bo = (JSONObject) parser.parse(json);
            return !(Boolean)bo.get("is_closed");
        } catch (ParseException pe) {
            logger.error("Error: could not parse JSON response:" + json);
            return true;
        }
    }

    public Restaurant addByBid(Long gid, String bid)throws Exception{
        YelpAPI ya = new YelpAPI();
        String json = ya.searchByBusinessId(bid);
        JSONParser parser = new JSONParser();
        JSONObject bo = null;
        try {
            bo = (JSONObject) parser.parse(json);
        } catch (ParseException pe) {
            logger.error("Error: could not parse JSON response:" + json);
            return null;
        }

        if(bo == null || bo.get("name") == null){
            logger.error("Bid not found: " + bid);
            return null;
        }
        String name = bo.get("name").toString();
        String url = bo.get("url").toString();
        Long factor = Math.round(Math.pow(2, Float.parseFloat(bo.get("rating").toString())));
        String cats = bo.get("categories").toString().toLowerCase();
        String cat = cats.split(",")[0];
        cat = cat.substring(cat.indexOf("\"") + 1, cat.lastIndexOf("\""));

        Restaurant r = new Restaurant(bid, url, factor, name,  cat);
        logger.debug("saving " + r.toString());
        Long rid = eb.saveRestaurant(r, null);
        Restaurant ret = eb.getRestaurant(new Pair<String, Object>("name", r.getName()));
        if(gid != null && gid > 0) {
            gb.saveRestaurantToGroup(rid, gid, factor);
        }

        return ret;
    }

    private void applySettings(String settings){
        String[] ss = settings.split(";");
        for (String s : ss){
            String key = StringUtils.trim(s.split("=")[0]);
            String value = StringUtils.trim(s.split("=")[1]);

            if(StringUtils.isBlank(key) || StringUtils.isBlank(value)){
                continue;
            }

            Class clazz = this.getClass();
            String getStr = "set" + StringUtils.capitalize(key);
            Method setMethod = null;
            try {
                setMethod = clazz.getMethod(getStr, String.class);
                setMethod.invoke(this, value);
            } catch (Exception e) {
                logger.error("Invalid attribute: " + key);
                continue;
            }
        }
    }

    private YelpAPI.YelpAPICLI getYaCli(){

        YelpAPI.YelpAPICLI yaCli = new YelpAPI.YelpAPICLI();
        yaCli.limit = 20;
        if (StringUtils.isNotBlank(location)) {
            yaCli.location = location;
        }
        if (StringUtils.isNotBlank(category)) {
            yaCli.cat = category;
        }
        if (StringUtils.isNotBlank(distance)) {
            yaCli.dis = distance;
        }
        return yaCli;
    }

    public static void main(String[] args) {
        try {
            new YelpRetriever().searchAndImport(29229L);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        new YelpRetriever().addByBid(null, "chef-yu-hunan-gourmet-sunnyvale");
    }

}
