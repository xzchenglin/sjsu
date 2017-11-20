package log;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by lin.cheng
 */
public class RedisUtils {

    public static final String PREFIX = "ES:";
    private static JedisPool jedisPool;
    private static JedisCluster jedisCluster;

    private static boolean cluster = true;
    private static int timeout = 180;
    private static String hostAndPorts = "ec2-34-192-241-153.compute-1.amazonaws.com:6666";

    static {

        List<HostAndPort> hosts = getHosts();
        cluster = hosts.size() > 1;
        if (!cluster) {
            HostAndPort host = hosts.get(0);
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setTestOnBorrow(true);
            poolConfig.setMaxTotal(32);
            poolConfig.setTestOnCreate(false);
            poolConfig.setTestOnBorrow(false);
            poolConfig.setMaxWaitMillis(10000);
            jedisPool = new JedisPool(poolConfig, host.getHost(), host.getPort());
        } else {
            jedisCluster = new JedisCluster(new HashSet<>(hosts), 1000, 1000, 6, new GenericObjectPoolConfig());
        }
    }

    public static void set(Long qid, String value) {
        JedisCommands j = getJedis();
        try {
            j.setex(buildKey(qid), timeout, value);
        } finally {
            close(j);
        }
    }

    public static void del(Long qid) {
        JedisCommands j = getJedis();
        try {
            j.del(buildKey(qid));
        } finally {
            close(j);
        }
    }

    public static void hset(Long qid, String field, Object value) {
        if (value == null) {
            return;
        }
        String vstr;
        if (!(value instanceof String)) {
            vstr = String.valueOf(value);
        } else {
            vstr = (String) value;
        }
        JedisCommands j = getJedis();
        try {
            j.hset(buildKey(qid), field, vstr);
            j.expire(buildKey(qid), timeout);
        } finally {
            close(j);
        }
    }

    public static void incr(Long qid, String field) {
        JedisCommands j = getJedis();
        try {
            j.incr(qid + "@" + field);
        } finally {
            close(j);
        }
    }

    public static long getNum(Long qid, String field) {
        JedisCommands j = getJedis();
        try {
            String s = j.get(qid + "@" + field);
            if(s == null){
                return 0;
            }
            return Long.parseLong(s);
        } finally {
            close(j);
        }
    }

    public static void hmset(Long qid, Map<String, String> hash) {
        JedisCommands j = getJedis();
        try {
            j.hmset(buildKey(qid), hash);
            j.expire(buildKey(qid), timeout);
        } finally {
            close(j);
        }
    }

    public static List<String> hmget(Long qid, String... fields) {
        List<String> ret;
        JedisCommands j = getJedis();
        try {
            ret = j.hmget(buildKey(qid), fields);
        } finally {
            close(j);
        }
        return ret;
    }

    public static String get(Long qid) {
        JedisCommands j = getJedis();
        try {
            return j.get(buildKey(qid));
        } finally {
            close(j);
        }
    }

    public static String hget(Long qid, String field) {
        JedisCommands j = getJedis();
        try {
            return j.hget(buildKey(qid), field);
        } finally {
            close(j);
        }
    }

    private static JedisCommands getJedis() {
        return cluster ? jedisCluster : jedisPool.getResource();
    }

    private static void close(JedisCommands j) {
        if (j instanceof Jedis) {
            ((Jedis) j).close();
        }
    }

    private static String buildKey(Long qid) {
        return PREFIX + "{" + qid + "}";
    }

    private static List<HostAndPort> getHosts() {
        List<HostAndPort> ret = new ArrayList<>();

        String[] hostArr = hostAndPorts.split(";");
        for(String h:hostArr){
            String[] p = h.split(":");
            String host = StringUtils.trim(p[0]);
            int port = p.length == 2 ? Integer.parseInt(StringUtils.trim(p[1])) : 6379;
            ret.add(new HostAndPort(host, port));
        }
        return ret;
    }
}
