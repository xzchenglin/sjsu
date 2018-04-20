import helper.Utils;

import static org.junit.Assert.assertNotNull;

public class UtilsTest {
    @org.junit.Test
    public void generateKeypair() throws Exception {
        String pk = Utils.generateKeypair("sjsu");
        System.out.println(pk);
        assertNotNull(pk);
    }

    @org.junit.Test
    public void enc() throws Exception {
        System.out.println(Utils.rsaEnc("C0DF2C2CABE44F7F720414E4F09FD624", "/opt/bc/site2.key"));
    }
}