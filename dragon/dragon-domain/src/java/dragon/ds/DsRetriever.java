package dragon.ds;

import dragon.model.food.Restaurant;

import java.util.List;

/**
 * Created by lin.cheng
 */
public interface DsRetriever {
    Restaurant addByBid(Long gid, String bid)throws Exception;
    List<Restaurant> searchAndImport(Long gid)throws Exception;
    boolean checkAndFill(Restaurant r)throws Exception;
}
