package logi.comm;

import static org.junit.Assert.assertNotNull;

public class UtilsTest {
    @org.junit.Test
    public void generateKeypair() throws Exception {
        String pk = Utils.generateKeypair("sjsu3");
        System.out.println(pk);
        assertNotNull(pk);
    }

    @org.junit.Test
    public void enc() throws Exception {
        System.out.println(Utils.rsaEnc("ooo", "/opt/295/keys/sjsu.key"));
    }
}