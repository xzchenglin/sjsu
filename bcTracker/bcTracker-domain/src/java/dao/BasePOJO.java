package dao;

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

    static protected SqlSessionFactory client;

    static {
        try (Reader reader = Resources.getResourceAsReader("sql-maps-config.xml")) {
            client = new SqlSessionFactoryBuilder().build((reader));
            PropertyConfigurator.configureAndWatch("/opt/bc/log4j.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
