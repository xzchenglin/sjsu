package dao;

import java.util.List;

/**
 * Created by Lin Cheng
 */
public interface BaseDao<T> {
    T create(T o) throws Exception;
    T update(T o) throws Exception;
    T find(String key) throws Exception;
    List<T> list(String kw) throws Exception;
}
