package db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by Lin Cheng
 */
public class BasePOJO {
    static Reader reader;
    static protected SqlSessionFactory client;

    static {
        try {
            reader = Resources.getResourceAsReader("sql-maps-config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client = new SqlSessionFactoryBuilder().build((reader));
        PropertyConfigurator.configureAndWatch("/opt/lghr/log4j.properties");
    }
}
