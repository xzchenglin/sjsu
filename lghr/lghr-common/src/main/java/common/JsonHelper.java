package common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonHelper {

    public static String toJson(Object obj){
//        if(obj == null){
//            return null;
//        }
//        String serialized = null;
//        try {
//            serialized = new ObjectMapper().writeValueAsString(obj);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return serialized;
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create().toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if(StringUtils.isBlank(json)){
            return null;
        }

        T obj =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create().fromJson(json, clazz);
        return obj;
    }
}