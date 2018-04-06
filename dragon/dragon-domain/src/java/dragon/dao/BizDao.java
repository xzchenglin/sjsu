package dragon.dao;

import dragon.comm.Pair;
import dragon.model.food.Record;
import dragon.model.food.Restaurant;
import dragon.model.food.Stat;
import dragon.model.food.Vote;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by lin.cheng
 */
public interface BizDao extends Dao {
    Restaurant pickRestaurant(Long gid)throws Exception;
    List<Restaurant> getRestaurants(String condition);
    List<Restaurant> getRestaurants(Long gid);
    Long saveRestaurant(Restaurant r, Connection conn);
    Restaurant vote(Vote v, boolean mute) throws Exception;
    Map<String, Stat> stat(long gid, long exId, Boolean sort);
    Map<String, Stat> stat2(long gid, int days);
    Record saveRecord(Record r) throws Exception;
    List<String> getMails(Long gid)throws Exception;
    Restaurant getRestaurant(Pair<String, Object> p);
    void printPerfData();
    List<Record> getRecords(Long gid, int limit) throws Exception;
    Record getRecord(Long recId) throws Exception;
}
