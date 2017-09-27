package main.java.common;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonHelper {

    public static String toJson(Object obj) throws Exception {
        if(obj == null){
            return null;
        }
        String serialized = new ObjectMapper().writeValueAsString(obj);

        return serialized;
    }
}
