package dragon.comm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lin.cheng
 */
public class ConfigHelper {
    static final String PATH = "/opt/dragon/config.txt";
    static public final String EN_PF = "en.";
    static volatile long fileTimestamp = 0L;
    static volatile boolean loading = false;

    private static ConfigHelper instance = new ConfigHelper();
    final Map<String, String> map = new HashMap<String, String>();

    private static final Object lock = new Object();
    static Log logger = LogFactory.getLog(ConfigHelper.class);

    private ConfigHelper() {
    }

    public static ConfigHelper instance() {
        long lastModified = new File(PATH).lastModified();

        if (lastModified > fileTimestamp) {
            synchronized (lock) {
                if (lastModified > fileTimestamp && !loading) {
                    logger.info("Config reloading...");
                    loading = true;
                    instance.load();
                    fileTimestamp = lastModified;
                    loading = false;
                }
            }
        }

        return instance;
    }

    private void load(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(PATH)));
            String line = null;

            map.clear();

            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }

                String[] property;
                String value = null;
                int index = line.indexOf("=");
                if (index > 0) {
                    property = new String[2];
                    property[0] = line.substring(0, index).trim();
                    value = line.substring(index + 1).trim();
                } else {
                    property = null;
                }
                if (property != null) {
                    map.put(property[0] ,value);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public String getConfig(String key) {
        return map.get(key);
    }

    public String getConfig(String key, String dft) {
        String ret = map.get(key);
        if(ret == null){
            return dft;
        }
        return ret;
    }
}
