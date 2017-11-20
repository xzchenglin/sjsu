package log;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class JsonHelper {

    public static String toJson(Object obj){
        if(obj == null){
            return null;
        }
        String serialized = null;
        try {
            serialized = new ObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serialized;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if(StringUtils.isBlank(json)){
            return null;
        }

        T obj = new Gson().fromJson(json, clazz);
        return obj;
    }
}