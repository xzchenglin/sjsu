package dragon.helper;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import dragon.comm.ConfigHelper;
import dragon.model.food.Vote;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyVetoException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin.cheng
 */
public class DbHelper {

    static Log logger = LogFactory.getLog(DbHelper.class);

    private static ComboPooledDataSource ds = null;
    private static Connection sc = null;
    private static final Object lock = new Object();
    private static final Object lock2 = new Object();

    public static ComboPooledDataSource getDs(){
        if(ds == null){
            synchronized(lock){
                if(ds == null){
                    //TODO - lookup from connection pool
                    String server = ConfigHelper.instance().getConfig("dbserver");
                    String db = ConfigHelper.instance().getConfig("db");
                    String dbuser = ConfigHelper.instance().getConfig("dbuser");
                    String dbpwd = ConfigHelper.instance().getConfig("dbpwd");

                    ds = new ComboPooledDataSource();
                    try {
                        ds.setDriverClass("org.postgresql.Driver");
                    } catch (PropertyVetoException e) {
                        logger.error("", e);
                        return null;
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
        logger.debug("Created :" + conn);
        return conn;
    }

    public static Connection getSharedConn() throws SQLException {
        if(sc == null || sc.isClosed()){
            synchronized(lock2){
                if(sc == null || sc.isClosed()){
                    sc = null;
                    sc = getDs().getConnection();
                }
            }
        }

        return sc;
    }

    public static void closeConn(Connection conn) {
        if(conn != null){
            try {
                logger.debug("Closed :" + conn);
                conn.close();
            } catch (SQLException e) {
                logger.error("", e);
            }
        }
    }

    public static <T> T runWithSingleResult(String sql, Connection conn) throws Exception{
        boolean reuse = conn != null;//Remember to close connection outside
        try {
            if(!reuse){
                conn = DbHelper.getConn();
            }
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()){
                return (T) rs.getObject(1);
            }
        } finally {
            if(!reuse){
                closeConn(conn);
            }
        }
        return null;
    }

    public static <T> T runWithSingleResult2(Connection conn, String sql,  Object ... params) throws Exception{
        boolean reuse = conn != null;//Remember to close connection outside
        try {
            if(!reuse){
                conn = DbHelper.getConn();
            }
            PreparedStatement st = conn.prepareStatement(sql);
            setParameters(st, params);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return (T) rs.getObject(1);
            }
        } finally {
            if(!reuse){
                closeConn(conn);
            }
        }
        return null;
    }

    public static <T> List<T> getFirstColumnList(Connection conn, String sql, Object... params) throws Exception{
        boolean reuse = conn != null;//Remember to close connection outside
        List<T> ret = new ArrayList<T>();
        try {
            if(!reuse){
                conn = DbHelper.getConn();
            }
            PreparedStatement st = conn.prepareStatement(sql);
            setParameters(st, params);
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                ret.add((T) rs.getObject(1));//First selected column only
            }
        } finally {
            if(!reuse){
                closeConn(conn);
            }
        }
        return ret;
    }

    public static int runUpdate(Connection conn, String sqlFmt, Object ... params) throws Exception{
        boolean reuse = conn != null;//Remember to close connection outside
        try {
            if(!reuse){
                conn = DbHelper.getConn();
            }
            Statement st = conn.createStatement();
            String sql = String.format(sqlFmt, params);
            int cnt = st.executeUpdate(sql);
            return cnt;
        } finally {
            if(!reuse){
                closeConn(conn);
            }
        }
    }

    public static int runUpdate2(Connection conn, String sql, Object ... params) throws Exception{
        boolean reuse = conn != null;//Remember to close connection outside
        try {
            if(!reuse){
                conn = DbHelper.getConn();
            }
            PreparedStatement st = conn.prepareStatement(sql);
            setParameters(st, params);
            int cnt = st.executeUpdate();
            return cnt;
        } finally {
            if(!reuse){
                closeConn(conn);
            }
        }
    }

    public static Long getNextId(Connection connection) throws Exception {
        Long id = DbHelper.runWithSingleResult("select nextval ('dragon_id_sec')", connection);
        return id;
    }

    public static void setParameters(final PreparedStatement st, Object ... params) throws SQLException {
        for(int i = 0; i<params.length; i++){
            if (params[i] == null){
                st.setNull(i+1, Types.NULL);
                continue;
            }
            Object p = params[i];
            if (p instanceof Integer || p instanceof Vote.Result){
                st.setInt(i + 1, (Integer) p);
            } else if(p instanceof Long){
                st.setLong(i + 1, (Long) p);
            } else if(p instanceof Boolean){
                st.setBoolean(i + 1, (Boolean) p);
            } else if(p instanceof String){
                st.setString(i + 1, (String) p);
            } else {
                throw new RuntimeException("Not supported type:" + p.getClass());
            }
        }
    }
}
