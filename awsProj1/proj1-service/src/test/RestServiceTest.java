import org.junit.Assert;
import org.junit.Test;
import service.camel.RestService;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

/**
 * Created by Lin Cheng
 */
public class RestServiceTest {

    RestService service = RestService.instance();

    @Test
    public void test() throws Exception {
        Timer timer = new Timer();
        final CountDownLatch latch = new CountDownLatch(1);
        timer.schedule(new TimerTask() {
                           public void run() {
                               try {
                                   service.stop();
                               } catch (Exception e) {
                                   e.printStackTrace();
                               }
                               latch.countDown();
                           }
                       },
                (5000));

        service.start();

        //cannot be here if stop failed
        Assert.assertTrue(true);
    }

}