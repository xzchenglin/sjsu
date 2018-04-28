package common;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by Lin Cheng on 2017/10/9.
 */
public class Utils {
    public static String readRawContentFromFile(String path) throws Exception {
        FileReader fr = null;
        BufferedReader rdr = null;
        try {
            String ret = "";
            fr = new FileReader(path);
            rdr = new BufferedReader(fr);
            String line;
            while ((line = rdr.readLine()) != null) {
                ret += line + "\n";
            }
            return ret;
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (Exception e) {
                }
                fr = null;
            }
            if (rdr != null) {
                try {
                    rdr.close();
                } catch (Exception e) {
                }
                rdr = null;
            }
        }
    }

}
