package dragon.dao;

import dragon.comm.ConfigHelper;
import dragon.comm.Pair;
import dragon.ds.DsRetriever;
import dragon.ds.YelpRetriever;
import dragon.helper.DbHelper;
import dragon.model.food.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.*;

/**
 * Created by lin.cheng
 */
public class BizBean implements BizDao {

    static Log logger = LogFactory.getLog(BizBean.class);
    static final Double GRAVITY = 0.35;
    static final Integer BASE = 0;

    //Thread safe
    public Long saveRestaurant(Restaurant r, Connection conn) {

        String key = r.getName();
        boolean reuse = conn != null;//Remember to close connection outside
        Long id = 0L;

        logger.info("Saving biz:" + r.getAlias());

        try {
            if (!reuse) {
                conn = DbHelper.getConn();
            }
            conn.setAutoCommit(false);
            Statement st1 = conn.createStatement();

            st1.execute("LOCK TABLE dragon_restaurant IN ACCESS EXCLUSIVE MODE");

            logger.debug("Locked: " + Thread.currentThread().getId());

            id = DbHelper.runWithSingleResult2(conn, "select id from dragon_restaurant where name =?", key);

            if (id != null) {
                Long exfactor = DbHelper.runWithSingleResult2(conn, "select factor from dragon_restaurant where name =?", key);
                if(Long.compare(r.getFactor(), exfactor) != 0) {
                    logger.info("Factor changed:" + r.getAlias());
                    DbHelper.runUpdate2(conn, "update dragon_restaurant set factor=? where name=?", r.getFactor(), key);
                }
                logger.debug("No qualified changes, skip:" + r.getAlias());
                //...need to add more if want to be able to update more columns
            } else {
                logger.info("Save new:" + r.getAlias());

                id = DbHelper.getNextId(conn);

                logger.debug("Add: " + Thread.currentThread().getId());

                DbHelper.runUpdate2(conn, "insert into dragon_restaurant (name,link,factor,id,category,alias,source) VALUES(?,?,?,?,?,?,?)",
                        key, r.getLink(), r.getFactor(), id, r.getCategory(), StringUtils.isNotBlank(r.getAlias()) ? r.getAlias() : key, r.getSource());
            }

            conn.commit();

            logger.debug("Commit and unlock: " + Thread.currentThread().getId());

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (!reuse) {
                DbHelper.closeConn(conn);
            }
        }

        return id;
    }

    //Not thread safe
    @Override
    public Restaurant vote(Vote v, boolean mute) throws Exception {

        Long t1 = System.currentTimeMillis();
        logger.info(v.toString());

        Long recId = v.getRecId();
        Record rec = getRecord(recId);
        if (rec == null) {
            logger.info("Record not found: " + recId);
            return null;
        }

        Long resId = rec.getResid();
        Restaurant res = getRestaurant(new Pair<String, Object>("id", resId));
        if (res == null) {
            logger.info("Restaurant not found: " + resId);
            return null;
        }

        Long t2 = System.currentTimeMillis();
        logger.debug("getRec takes: " + (t2 - t1));

        saveVote(v, null);

        Long t3 = System.currentTimeMillis();
        logger.debug("saveVote takes: " + (t3 - t2));

        if (v.getResult() == Vote.Result.killme) {

            rec.setVeto(true);
            try {
                saveRecord(rec);
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
            Restaurant r= pickRestaurant(rec.getgId());

            if(mute){
                //TODO
            }

            return r;
        } else {
            logger.info("Already vetoed or time passed.");
            return null;
        }
    }


    public List<Restaurant> getRestaurants(String condition) {
        Connection conn = null;
        List<Restaurant> list = new ArrayList<Restaurant>();
        try {
            conn = DbHelper.getConn();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from dragon_restaurant" + (StringUtils.isBlank(condition) ? "" : " where " + condition));

            while (rs.next()) {
                list.add(new Restaurant(rs.getString("name"), rs.getString("link"), rs.getLong("factor"), rs.getLong("id")
                ,rs.getString("alias"), rs.getString("category"), rs.getString("source")));
            }
        } catch (Exception e) {
            logger.error("");
        } finally {
            DbHelper.closeConn(conn);
        }
        return list;
    }

    public List<Restaurant> getRestaurants(Long gid) {
        Connection conn = null;
        List<Restaurant> list = new ArrayList<Restaurant>();
        try {
            conn = DbHelper.getConn();
            PreparedStatement st = conn.prepareStatement("select r.name,r.link,gr.factor,r.id,r.alias,r.category,r.source from dragon_restaurant r,dragon_group g,dragon_group_rest gr " +
                    "where gr.res_id=r.id and gr.g_id=g.id and g.id=? and gr.factor>0");
            DbHelper.setParameters(st, gid);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                list.add(new Restaurant(rs.getString("name"), rs.getString("link"), rs.getLong("factor"), rs.getLong("id")
                        ,rs.getString("alias"), rs.getString("category"), rs.getString("source")));
            }
        } catch (Exception e) {
            logger.error("");
        } finally {
            DbHelper.closeConn(conn);
        }
        return list;
    }

    public Restaurant pickRestaurant(Long gid)throws Exception {

        Long t1 = System.currentTimeMillis();

        List<Restaurant> list = getRestaurants(gid);
        if (list == null || list.size() == 0) {
            logger.info("Restaurant not found.");
            return null;
        }

        Long t2 = System.currentTimeMillis();
        logger.debug("getRestaurants takes: " + (t2 - t1));

        List<Long> preIds = DbHelper.getFirstColumnList(null, "select distinct res_id,id from dragon_record where g_id=" + gid + " order by id desc limit ?",
                Integer.parseInt(ConfigHelper.instance().getConfig("excludepre", "5")));

        Long t3 = System.currentTimeMillis();
        logger.debug("stat takes: " + (t3 - t2));

        Restaurant r = pickFrom(list, preIds, gid);
        if (r == null) {
            logger.info("Biz not found.");
            return null;
        }

        Record rec = new Record();
        rec.setResid(r.getId());
        rec.setgId(gid);
        Long id = saveRecord(rec).getId();
        r.setRecId(id);

        return r;
    }

    private Restaurant pickFrom(List<Restaurant> list, List<Long> preIds, Long gid){

        if(list == null || list.isEmpty()){
            return null;
        }

        Map<String, Stat> ss = stat(gid, 0, false);
        long totalWeight = 0;

        for (Restaurant r : list) {
            if (preIds.contains(r.getId()) && list.size() > preIds.size()) {
                continue;
            }
            totalWeight += getWeight(ss, r);
        }

        long rdm = Math.abs(new Random().nextLong());
        long selected = rdm % totalWeight;
        long pos = 0;
        for (Restaurant r : list) {

            if (preIds.contains(r.getId()) && list.size() > preIds.size()) {
                logger.debug(r.getAlias() + " skipped.");
                continue;
            }

            if (pos <= selected && selected < pos + getWeight(ss, r)) {
                Long t4 = System.currentTimeMillis();
                logger.info("Picked up: " + r.getAlias());

                DsRetriever ds = null;
                if("y".equals(r.getSource())){
                    ds = new YelpRetriever();
                }

                if(ds == null){
                    return r;
                }

                try {
                    Restaurant rr = ds.find(r.getName());
                    if(rr == null){
                        return r;
                    }
                    if(rr.getOpen() != null && rr.getOpen() == false){
                        logger.info("Currently closed: " + r.getAlias());
                        list.remove(r);
                        return pickFrom(list, preIds, gid);
                    } else {
                        r.setLink(rr.getLink());
                        r.setAlias(rr.getAlias());
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }

                return r;
            }
            pos += getWeight(ss, r);
        }

        logger.info("Not able to find a restaurant.");
        return null;

    }

    public Map<String, Stat> stat(long gid, long exId, Boolean sort) {
        Connection conn = null;
        Map<String, Stat> ret = new HashMap<String, Stat>();

        try {
            conn = DbHelper.getConn();
            PreparedStatement st = conn.prepareStatement("select res.name, res.alias,res.factor,v.vote,count(*) from dragon_restaurant res " +
                    "inner join dragon_record r on r.res_id=res.id left join dragon_vote v on v.rec_id=r.id " +
                    "where r.g_id =? group by res.name,res.alias,res.factor,v.vote order by count(*)");
            DbHelper.setParameters(st, gid);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String alias = rs.getString("alias");
                int factor = rs.getInt("factor");
                int score = BASE;
                Object vote = rs.getObject("vote");
                int cnt = rs.getInt("count");

                Vote.Result vr = vote == null ? null : Vote.Result.values()[(Integer) vote];
                Stat s = null;
                if (!ret.containsKey(name)) {
                    s = new Stat(name, factor, score);
                    s.setAlias(alias);
                    ret.put(name, s);
                } else {
                    s = ret.get(name);
                }

                if (vr == Vote.Result.dislike) {
                    s.setDisliked(s.getDisliked() + cnt);
                }
                if (vr == Vote.Result.like) {
                    s.setLiked(s.getLiked() + cnt);
                }
                if (vr == Vote.Result.killme) {
                    s.setVetoed(s.getVetoed() + cnt);
                }
                if(vr != null) {
                    s.setScore(s.getScore() + vr.getScore() * cnt);
                }
            }

            PreparedStatement st2 = conn.prepareStatement("select res.name,count(*) from dragon_restaurant res inner join dragon_record r on r.res_id=res.id " +
                            "where r.veto = false and r.g_id = ? and r.id <> ? group by res.name");
            DbHelper.setParameters(st2, gid, exId);
            rs = st2.executeQuery();

            while (rs.next()){
                String name = rs.getString("name");
                int cnt = rs.getInt("count");

                if(ret.containsKey(name)){
                    Stat s = ret.get(name);
                    s.setVisited(cnt);
                    if(s.getFactor() > 0){
                        s.setScore(s.getScore() + (int) Math.round(cnt / Math.pow(s.getFactor(), GRAVITY)));
                    }
                }
            }

            setGroupFactor(conn, gid, ret);

            if(sort) {
                ValueComparator bvc = new ValueComparator(ret);
                Map<String, Stat> sorted = new TreeMap<String, Stat>(bvc);
                sorted.putAll(ret);

                return sorted;
            } else {
                return ret;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            DbHelper.closeConn(conn);
        }

        return ret;
    }

    public Map<String, Stat> stat2(long gid, int days) {
        Connection conn = null;
        Map<String, Stat> ret = new HashMap<String, Stat>();
        long gotime = System.currentTimeMillis() - 1000*3600*24L * days;

        try {
            conn = DbHelper.getConn();
            PreparedStatement st = conn.prepareStatement("select res.name,res.factor,v.vote,count(*) from dragon_restaurant res " +
                    "inner join dragon_record r on r.res_id=res.id left join dragon_vote v on v.rec_id=r.id " +
                    "where r.g_id=? and r.go_time>? group by res.name,res.factor,v.vote order by count(*)");
            DbHelper.setParameters(st, gid, gotime);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                int factor = rs.getInt("factor");
                Object vote = rs.getObject("vote");
                int cnt = rs.getInt("count");

                Vote.Result vr = vote == null ? null : Vote.Result.values()[(Integer) vote];
                Stat s = null;
                if (!ret.containsKey(name)) {
                    s = new Stat(name, factor, 0);
                    ret.put(name, s);
                } else {
                    s = ret.get(name);
                }

                if (vr == Vote.Result.dislike) {
                    s.setDisliked(s.getDisliked() + cnt);
                }
                if (vr == Vote.Result.like) {
                    s.setLiked(s.getLiked() + cnt);
                }
                if (vr == Vote.Result.killme) {
                    s.setVetoed(s.getVetoed() + cnt);
                }
                if(vr != null) {
                    s.setScore(s.getScore() + vr.getScore() * cnt);
                }
            }

            PreparedStatement st2 = conn.prepareStatement("select res.name,count(*) from dragon_restaurant res inner join dragon_record r on r.res_id=res.id " +
                    "where r.veto = false and r.g_id =? and r.go_time > ? group by res.name");
            DbHelper.setParameters(st2, gid, gotime);
            rs = st2.executeQuery();

            while (rs.next()){
                String name = rs.getString("name");
                int cnt = rs.getInt("count");

                if(ret.containsKey(name)){
                    Stat s = ret.get(name);
                    s.setVisited(cnt);
                    if(s.getFactor() > 0){
                        s.setScore(s.getScore() + (int) Math.round(cnt / Math.pow(s.getFactor(), GRAVITY)));
                    }
                }
            }

            setGroupFactor(conn, gid, ret);

            ValueComparator bvc =  new ValueComparator(ret);
            Map<String, Stat> sorted = new TreeMap<String, Stat>(bvc);
            sorted.putAll(ret);

            return sorted;
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            DbHelper.closeConn(conn);
        }

        return ret;
    }

    private void setGroupFactor(Connection conn, long gid, final Map<String, Stat> ss) throws SQLException {

        if(conn == null || ss == null || ss.size() == 0){
            return;
        }

        PreparedStatement st = conn.prepareStatement("select r.name,gr.factor from dragon_restaurant r inner join dragon_group_rest gr on r.id=gr.res_id where gr.g_id=?");
        DbHelper.setParameters(st, gid);
        ResultSet rs = st.executeQuery();

        while (rs.next()){
            String name = rs.getString("name");
            Integer factor = rs.getInt("factor");

            if(ss.containsKey(name)){
                Stat s = ss.get(name);
                if(factor != null && factor >= 0){
                    s.setFactor(factor);
                }
            }
        }

    }

    public Record saveRecord(Record r) throws Exception {

        Long id = r.getId();
        if (id == null || id <= 0) {
            id = DbHelper.getNextId(null);
            DbHelper.runUpdate2(null, "insert into dragon_record (id,res_id,go_time,g_id) VALUES(?,?,?,?)",
                    id, r.getResid(), System.currentTimeMillis(), r.getgId());
        } else {
            DbHelper.runUpdate2(null, "update dragon_record set veto=? where id=?", r.getVeto(), r.getId());
        }
        return getRecord(id);
    }

    public Record getRecord(Long recId) throws Exception{
        Connection conn = null;
        Record rec = null;

        try {
            conn = DbHelper.getConn();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from dragon_record where id = " + recId);

            if (rs.next()) {
                rec = new Record();
                rec.setId(recId);
                rec.setVeto(rs.getBoolean("veto"));
                rec.setResid(rs.getLong("res_id"));
                rec.setGoTime(rs.getLong("go_time"));
                rec.setgId(rs.getLong("g_id"));
            }

            return rec;
        } finally {
            DbHelper.closeConn(conn);
        }
    }

    public List<Record> getRecords(Long gid, int limit) throws Exception {
        Connection conn = null;

        try {
            conn = DbHelper.getConn();
            List<Record> list = new ArrayList<Record>();

            String sql = "select * from dragon_record where g_id = ? order by go_time desc";
            if(limit > 0){
                sql += " limit " + limit;
            }
            PreparedStatement st = conn.prepareStatement(sql);
            DbHelper.setParameters(st, gid);

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Record rec = new Record();
                rec.setId(rs.getLong("id"));
                rec.setVeto(rs.getBoolean("veto"));
                rec.setResid(rs.getLong("res_id"));
                rec.setGoTime(rs.getLong("go_time"));
                rec.setgId(rs.getLong("g_id"));

                list.add(rec);
            }

            return list;
        } finally {
            DbHelper.closeConn(conn);
        }
    }

    public Restaurant getRestaurant(Pair<String, Object> p) {
        Connection conn = null;
        Restaurant ret = null;

        try {
            conn = DbHelper.getConn();
            PreparedStatement st = conn.prepareStatement("select * from dragon_restaurant where " + p.getLeft() + "= ?");
            DbHelper.setParameters(st, p.getRight());
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                ret = new Restaurant(rs.getString("name"), rs.getString("link"), rs.getLong("factor"), rs.getLong("id")
                        , rs.getString("alias"), rs.getString("category"), rs.getString("source"));
            }

            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        } finally {
            DbHelper.closeConn(conn);
        }
    }

    public Restaurant getRestaurantById(Long id) {
        return getRestaurant(new Pair<String, Object>("id", id));
    }

    private Boolean saveVote(Vote r, Connection conn) {

        String mail = r.getEmail();
        String ip = r.getIp();
        Long rid = r.getRecId();
        int res = r.getResult().ordinal();

        boolean reuse = conn != null;//Remember to close connection outside

        try {
            if (!reuse) {
                conn = DbHelper.getConn();
            }

            Object obj = DbHelper.runWithSingleResult2(conn, "select vote from dragon_vote where email = ? and rec_id =?", mail, rid);
            if (obj != null) {
                if (res != (Integer) obj) {
                    DbHelper.runUpdate2(conn, "update dragon_vote set vote=?,ip=? where email = ? and rec_id=?", res, ip, mail, rid);
                } else {
                    return false;
                }
            } else {
                DbHelper.runUpdate2(conn, "insert into dragon_vote (rec_id,vote,email,ip) VALUES(?,?,?,?)", rid, res, mail, ip);
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (!reuse) {
                DbHelper.closeConn(conn);
            }
        }

        return true;
    }

    public List<String> getMails(Long gid)throws Exception {
        List<String> mails = DbHelper.getFirstColumnList(null, "select u.email from dragon_user u, dragon_group g, dragon_group_user gu where gu.u_id=u.id and gu.g_id=g.id and g.id=? and gu.mute != true", gid);
        return mails;
    }

    public void printPerfData(){
    }

    private long getWeight(Map<String, Stat> ss, Restaurant r){
        String name = r.getName();
        long ret = r.getWeight() + BASE;
        if(ss.get(name) != null){
            ret = r.getWeight() + ss.get(name).getScore();
            if(ret < 1){
                ret = 1;
            }
        }
        return ret;
    }

    static class ValueComparator implements Comparator<String> {

        Map<String, Stat> base;
        public ValueComparator(Map<String, Stat> base) {
            this.base = base;
        }

        public int compare(String a, String b) {
            if(base.get(a) == null || base.get(b) == null){
                return 1;
            }
            int ret = base.get(b).getVisited() - base.get(a).getVisited();
            if(ret == 0) {
                return a.compareTo(b);
            }
            return ret;
        }
    }
}
