package dragon.dao;

import dragon.comm.Pair;
import dragon.ds.DsRetriever;
import dragon.ds.YelpRetriever;
import dragon.helper.DbHelper;
import dragon.model.food.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lin.cheng
 */
public class GroupBean implements GroupDao {

    static Log logger = LogFactory.getLog(GroupBean.class);

    public Group saveGroup(Group g) throws Exception{

        if(g == null) {
            return null;
        }

        Boolean isNew = g.getId() == null || g.getId() <= 0;

        if(isNew){
            logger.info("Adding group " + g.getName());

            String key = g.getName();
            Group ret = getGroup(new Pair<String, Object>("name", key));
            if(ret != null) {
//                throw new RuntimeException("Group already exists: " + key);
                logger.warn("Group already exists: " + key);
                return ret;
            }
            Long id = DbHelper.getNextId(null);
            DbHelper.runUpdate2(null, "insert into dragon_group (id,name,preference,active,no_approve,alias) VALUES(?,?,?,?,?,?)",
                    id, g.getName(), g.getPreference(), g.getActive(), g.getNoApprove(), g.getAlias());
        } else {
            logger.info("Updating group " + g.getName());

            DbHelper.runUpdate2(null, "update dragon_group set preference=?,no_approve=?,alias=?,active=? where id=?",
                    g.getPreference(), g.getNoApprove(), g.getAlias(), g.getActive(), g.getId());
        }

        Group ret = getGroup(new Pair<String, Object>("name", g.getName()));
        return ret;
    }

    public int applyPreference(Group g){

        logger.info("Applying: " + g.getName());

        if(g == null){
            return -1;
        }

        int cnt = 0;
        DsRetriever dr = new YelpRetriever(g.getPreference());
        try {
            List<Restaurant> ret = dr.searchAndImport(g.getId());
            g.setRestaurants(ret);
            if(ret != null) {
                cnt = ret.size();
            }
        } catch (Exception e){
            logger.error("Failed to run data retriever: ", e);
        }

        return cnt;
    }

    public List<Group> getGroups(String mail) {
        Connection conn = null;
        List<Group> list = new ArrayList<Group>();
        try {
            conn = DbHelper.getConn();
            PreparedStatement st = null;
            st = conn.prepareStatement("select * from dragon_group g, dragon_group_user gu, dragon_user u " +
                    "where g.id=gu.g_id and u.id=gu.u_id and u.email=?");
            DbHelper.setParameters(st, mail);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(new Group(rs.getLong("id"), rs.getString("name"), rs.getString("alias"), rs.getString("preference"),
                        rs.getBoolean("active"), rs.getBoolean("no_approve"), rs.getBoolean("admin")));
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            DbHelper.closeConn(conn);
        }
        return list;
    }

    public Group getGroup(Pair<String, Object> p){
        Connection conn = null;
        Group g = null;

        try {
            conn = DbHelper.getConn();
            PreparedStatement st = conn.prepareStatement("select * from dragon_group where " + p.getLeft() + "= ?");
            DbHelper.setParameters(st, p.getRight());
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                g = new Group(rs.getLong("id"), rs.getString("name"), rs.getString("alias"), rs.getString("preference"),
                        rs.getBoolean("active"), rs.getBoolean("no_approve"));
                return g;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            DbHelper.closeConn(conn);
        }

        return g;
    }

    public Restaurant addByBizId(String yid, Long gid)throws Exception{
        DsRetriever dr = new YelpRetriever();
        Restaurant ret = dr.addByBid(gid, yid);
        return ret;
    }

    public int saveUserToGroup(String email, String gname, boolean admin)throws Exception {
        Long gid = DbHelper.runWithSingleResult2(null, "select id from dragon_group where name = ?", gname);
        int cnt = saveUserToGroup(email, gid, admin);
        return cnt;
    }

    public int removeUserFromGroup(String email, String gname)throws Exception {

        Long gid = DbHelper.runWithSingleResult2(null, "select id from dragon_group where name = ?", gname);
        int cnt = removeUserFromGroup(email, gid);
        return cnt;
    }

    public int saveUserToGroup(String email, Long gid, boolean admin)throws Exception {

        logger.info("Adding user: " + email + " -> " + gid);

        int cnt = 0;
        Long uid = DbHelper.runWithSingleResult2(null, "select id from dragon_user where email = ?", email);
        Boolean ex =  DbHelper.runWithSingleResult2(null, "select admin from dragon_group_user where u_id = ? and g_id = ?", uid, gid);
        if(ex == null) {
            cnt = DbHelper.runUpdate2(null, "insert into dragon_group_user (g_id,u_id,admin) VALUES(?,?,?)",
                    gid, uid, admin);
        } else if(ex == false && admin == true) {//Do not change admin to false
            cnt = DbHelper.runUpdate2(null, "update dragon_group_user set admin=? where g_id=? and u_id=?", admin, gid, uid);
        }

        return cnt;
    }

    public int removeUserFromGroup(String email, Long gid)throws Exception {

        logger.info("Removing user: " + email + " -> " + gid);

        Long uid = DbHelper.runWithSingleResult2(null, "select id from dragon_user where email = ?", email);
        int cnt = DbHelper.runUpdate2(null, "delete from dragon_group_user where g_id=? and u_id=?", gid, uid);
        return cnt;
    }

    public int saveRestaurantByName(Long rid, String gname, Long factor)throws Exception {

        Long gid = DbHelper.runWithSingleResult2(null, "select id from dragon_group where name = ?", gname);
        int cnt = saveRestaurantToGroup(rid, gid, factor);

        return cnt;
    }

    public int saveRestaurantToGroup(Long rid, Long gid, Long factor)throws Exception {

        logger.info("Saving biz: " + rid + " -> " + gid);

        int cnt = 0;
        Long ex =  DbHelper.runWithSingleResult2(null, "select factor from dragon_group_rest where res_id = ? and g_id = ?", rid, gid);
        if(ex == null) {
            logger.info("Adding biz: " + rid + " -> " + gid);
            cnt = DbHelper.runUpdate2(null, "insert into dragon_group_rest (g_id,res_id,factor) VALUES(?,?,?)", gid, rid, factor);
        } else if(Long.compare(ex, factor) != 0 && factor > 0) {
            logger.info("Factor changed:" + rid + "|" + gid);
            cnt = DbHelper.runUpdate2(null, "update dragon_group_rest set factor=? where g_id=? and res_id=?", factor, gid, rid);
        } else {
            logger.debug("Factor not changed:" + rid + "|" + gid);
        }

        return cnt;
    }

    public int saveRestaurantToGroupBatch(List<Pair> pair, Long gid) throws Exception {

        logger.info("Saving factors for: " + gid);
        Connection conn = null;

        try {
            conn = DbHelper.getConn();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select res_id,factor from dragon_group_rest where g_id = " + gid);
            Map<Long, Long> existing = new HashMap<Long, Long>();
            while (rs.next()) {
                existing.put(rs.getLong("res_id"), rs.getLong("factor"));
            }

            conn.setAutoCommit(false);

            int cnt = 0;
            PreparedStatement st1 = conn.prepareStatement("insert into dragon_group_rest (g_id,res_id,factor) VALUES(?,?,?)");
            PreparedStatement st2 = conn.prepareStatement("update dragon_group_rest set factor=? where g_id=? and res_id=?");

            for(Pair p:pair){
                Long rid = new Double(p.getLeft().toString()).longValue();
                Long factor =  new Double(p.getRight().toString()).longValue();
                Long ex = existing.get(rid);

                if (ex == null) {
                    logger.info("Adding biz: " + rid + " -> " + gid);
                    st1.setLong(1, gid);
                    st1.setLong(2, rid);
                    st1.setLong(3, factor);
                    st1.addBatch();
                    cnt ++;
                } else if (Long.compare(ex, factor) != 0 && factor > 0) {
                    logger.info("Factor changed:" + rid + "|" + gid);
                    st2.setLong(2, gid);
                    st2.setLong(3, rid);
                    st2.setLong(1, factor);
                    st2.addBatch();
                    cnt ++;
                } else {
                    logger.debug("Factor not changed:" + rid + "|" + gid);
                }
            }

            st1.executeBatch();
            st2.executeBatch();
            conn.commit();

            return cnt;
        }finally {
            DbHelper.closeConn(conn);
        }
    }

    public int removeRestaurantFromGroup(Long rid, Long gid) throws Exception{

        logger.info("Removing biz: " + rid + " -> " + gid);

        int cnt = DbHelper.runUpdate2(null, "delete from dragon_group_rest where g_id=? and res_id=?", gid, rid);
        return cnt;
    }

    public Boolean subscribe(String email, Long gid, boolean sub) throws Exception{

        return subscribe(email, gid, sub, false);
    }

    //Not thread safe
    public Boolean subscribe(String email, Long gid, boolean sub, boolean admin)throws Exception {

        if (StringUtils.isBlank(email)) {//TODO
//            email = curentUser;
        }

        if (StringUtils.isBlank(email)) {
            return false;
        }

        logger.info(email + "->" + gid + (sub ? " subing..." : " unsubing..."));

        Long uid = DbHelper.runWithSingleResult2(null, "select id from dragon_user where email = ?", email);
        if (uid == null) {
            DbHelper.runUpdate2(null, "insert into dragon_user (id,email,name) VALUES(?,?,?)",
                    DbHelper.getNextId(null), email, email);
        }

        int cnt = 0;
        if(gid != null && gid > 0) {
            if (sub) {
                cnt = saveUserToGroup(email, gid, admin);
            } else {
                cnt = removeUserFromGroup(email, gid);
            }
        }

        return cnt > 0;
    }

    public Boolean mute(Long gid, boolean mute)throws Exception{
//        Long uid= AccessController.getCurrentUserId();
//        DbHelper.runUpdate2(null, "update dragon_group_user set mute=? where g_id=? and u_id=?", mute, gid, uid);
        return mute;
    }

    public Long saveUser(User u, boolean reg)throws Exception {

        logger.info("Saving user: " + u.getName());

        String key = u.getName();
        Long id = DbHelper.runWithSingleResult2(null, "select id from dragon_user where name = ?", key);

        if(reg){
            if(id != null){
//                throw new ApplicationException("User is already existing.");
            }
        }

        if(id != null) {
            int cnt = DbHelper.runUpdate2(null, "update dragon_user set alias=?,email=? where name=?",
                    u.getAlias(), u.getEmail(), key);

            if (cnt > 0) {
//                getIdentMgr().createOrUpdate(u.getName(), u.getPwd(), true);
                return id;
            }
        }

        id = DbHelper.getNextId(null);
        int cnt = DbHelper.runUpdate2(null, "insert into dragon_user (id,email,name,alias) VALUES(?,?,?,?)",
                id, u.getEmail(), u.getName(),u.getAlias());

        if(cnt > 0) {
//            getIdentMgr().createOrUpdate(u.getName(), u.getPwd(), true);
        }
        return id;
    }

    public void loadDependencies(Group g, int limit) throws Exception{
        if(g == null) return;

        Long gid = g.getId();

        BizDao eb = Daos.get(BizDao.class);

        List<Record> recs = eb.getRecords(gid, limit);
        g.setRecords(recs);

        Map<String, Stat> stats =  eb.stat(gid, 0, true);
        g.setStats(stats);

        List<Restaurant> rests = eb.getRestaurants(gid);
        g.setRestaurants(rests);

        if(isMember()) {
            List<User> users = getUsers(gid);
            g.setUsers(users);
        }
    }

    private boolean isMember() {
        return true;//TODO
    }

    public User getUser(String name) {
        Connection conn = null;
        User rec = null;

        try {
            conn = DbHelper.getConn();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from dragon_user where name = '" + name + "'");

            if (rs.next()) {
                rec = new User();
                rec.setId(rs.getLong("id"));
                rec.setEmail(rs.getString("email"));
                rec.setAlias(rs.getString("alias"));
                rec.setName(name);
            }

            return rec;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            DbHelper.closeConn(conn);
        }
    }

    public List<User> getUsers(Long gid) throws Exception {
        Connection conn = null;
        List<User> list = new ArrayList<User>();

        try {
            conn = DbHelper.getConn();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from dragon_user,dragon_group_user where " +
                    "dragon_group_user.u_id=dragon_user.id and dragon_group_user.g_id=" + gid);

            while (rs.next()) {
                User rec = new User();
                rec.setId(rs.getLong("id"));
                rec.setEmail(rs.getString("email"));
                rec.setName(rs.getString("name"));
                rec.setAlias(rs.getString("alias"));

                list.add(rec);
            }

            return list;
        } finally {
            DbHelper.closeConn(conn);
        }
    }

}
