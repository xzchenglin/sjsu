package common;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;

public class JsonHelper {

    public static String toJson(Object obj){
        if(obj == null){
            return null;
        }
        String serialized = null;
        try {
            serialized = new ObjectMapper().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL).writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serialized;
    }
}