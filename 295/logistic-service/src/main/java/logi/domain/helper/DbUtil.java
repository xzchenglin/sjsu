package logi.domain.helper;

import javafx.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class DbUtil {

    public static Query buildQuery(String sql, List<Pair> pairs, EntityManager em){

        if(!CollectionUtils.isEmpty(pairs)){
            sql += " WHERE ";
            for(Pair p:pairs) {
                sql += " " + p.getKey() + " = ?";
            }
        }

        Query query = em.createNativeQuery(sql);

        if(!CollectionUtils.isEmpty(pairs)){
            int index = 1;
            for(Pair p:pairs) {
                query.setParameter(index++, p.getValue());
            }
        }

        return query;
    }

    public static String prepareSels(String sels, final Map<String, Integer> sel2IndexMap, String[] alias){
        String[] selArr = sels.split(",");
        int a = -1;
        String[] ret = new String[selArr.length];

        for(int i=0; i<selArr.length; i++){
            String f = selArr[i];
            if("id".equals(f)) a++;
            sel2IndexMap.put(alias[a] + "_" + f, i);
            ret[i] = (alias[a] + "." + f + " AS " + alias[a] + "_" + f);
        }
        return StringUtils.join(ret, ",");
    }
}
