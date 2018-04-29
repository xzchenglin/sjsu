package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    public static String toJson(Object obj) {
        if(obj == null){
            return null;
        }
        String serialized = new Gson().toJson(obj);

        return serialized;
    }

    public static Object fromJson(String json, Class clazz)  {
        if(StringUtils.isBlank(json)){
            return null;
        }

        Object obj = new Gson().fromJson(json, clazz);
        return obj;
    }

    public static <T> T fromJson2(String json, Class<T> clazz) {
        if(StringUtils.isBlank(json)){
            return null;
        }
        T obj = new Gson().fromJson(json, clazz);
        return obj;
    }

    public static <T> T fromJsonWithTypeAdpater(String json, Class<T> clazz, Gson gsonWithAdapter)  {
        if(StringUtils.isBlank(json)){
            return null;
        }
        T obj = gsonWithAdapter.fromJson(json, clazz);
        return obj;
    }

    public static <T> List<T> fromJsonForObjList(String json, Class<T> clazz)  {
        List<T> ret = new ArrayList<T>();
        if(StringUtils.isBlank(json)){
            return ret;
        }
        List list = fromJson2(json, ArrayList.class);
        for (Object obj : list) {
            T item = fromJson2(toJson(obj), clazz);
            ret.add(item);
        }
        return ret;
    }
}