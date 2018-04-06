package dragon.dao;

import java.util.HashMap;
import java.util.Map;

public class Daos {

    private static Map<Class, Object> cache = new HashMap<>();

    static {
        cache.put(BizDao.class, new BizBean());
        cache.put(GroupDao.class, new GroupBean());
    }

    public static <T extends Dao> T get(Class<T> clazz){
        if(cache.get(clazz) == null){
            return null;
        }
        return (T) cache.get(clazz);
    }
}

