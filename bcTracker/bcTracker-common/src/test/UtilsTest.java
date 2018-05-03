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
        System.out.println(Utils.rsaEnc("E1627CC93AD4268C2D35185BD198D1B9", "/opt/bc/site1.key"));
    }
}