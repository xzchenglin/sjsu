import static org.junit.Assert.assertNotNull;

public class UtilsTest {
    @org.junit.Test
    public void generateKeypair() throws Exception {
        String pk = Utils.generateKeypair();
        System.out.println(pk);
        assertNotNull(pk);
    }

}