package helper;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin.cheng
 */
public class JSONHelper {

    public static String toJson(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        String serialized = new Gson().toJson(obj);

        return serialized;
    }

    public static Object fromJson(String json, Class clazz) throws Exception {
        if(StringUtils.isBlank(json)){
            return null;
        }

        Object obj = new Gson().fromJson(json, clazz);
        return obj;
    }

    public static <T> T fromJson2(String json, Class<T> clazz) throws Exception {
        if(StringUtils.isBlank(json)){
            return null;
        }
        T obj = new Gson().fromJson(json, clazz);
        return obj;
    }

    public static <T> T fromJsonWithTypeAdpater(String json, Class<T> clazz, Gson gsonWithAdapter) throws Exception {
        if(StringUtils.isBlank(json)){
            return null;
        }
        T obj = gsonWithAdapter.fromJson(json, clazz);
        return obj;
    }

    public static <T> List<T> fromJsonForObjList(String json, Class<T> clazz) throws Exception {
        List<T> ret = new ArrayList<T>();
        if(StringUtils.isBlank(json)){
            return ret;
        }
        List list = JSONHelper.fromJson2(json, ArrayList.class);
        for (Object obj : list) {
            T item = JSONHelper.fromJson2(JSONHelper.toJson(obj), clazz);
            ret.add(item);
        }
        return ret;
    }
}
