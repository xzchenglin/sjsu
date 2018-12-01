package service;

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
}