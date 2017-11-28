package domain.helper;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Lin Cheng
 */
public class DbHelper {
    private static ComboPooledDataSource ds = null;
    private static final Object lock = new Object();

    public static ComboPooledDataSource getDs(){
        if(ds == null){
            synchronized(lock){
                if(ds == null){
                    String server = ConfigHelper.instance().getConfig("dbserver");
                    String db = ConfigHelper.instance().getConfig("db");
                    String dbuser = ConfigHelper.instance().getConfig("dbuser");
                    String dbpwd = ConfigHelper.instance().getConfig("dbpwd");

                    ds = new ComboPooledDataSource();
                    try {
                        ds.setDriverClass("org.postgresql.Driver");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ds.setJdbcUrl("jdbc:postgresql://" + server + "/" + db);
                    ds.setUser(dbuser);
                    ds.setPassword(dbpwd);
                }
            }
        }

        return ds;
    }

    public static Connection getConn() throws SQLException {
        Connection conn = getDs().getConnection();
        return conn;
    }

    public static void closeConn(Connection conn) {
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T runWithSingleResult(String sql){
        Connection conn = null;
        try {
            conn = getConn();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                return (T) rs.getObject(1);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            closeConn(conn);
        }
        return null;
    }

}
