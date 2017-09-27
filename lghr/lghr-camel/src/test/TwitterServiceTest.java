import camel.TwitterService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

/***
 * Created by Lin Cheng on 2017/9/25.
 */
public class TwitterServiceTest {

    TwitterService service = TwitterService.instance();

    @Test
    //put everything here since they should be in the same instance
    public void all() throws Exception {
        Timer timer = new Timer();
        final CountDownLatch latch = new CountDownLatch(1);
        timer.schedule(new TimerTask() {
                           public void run() {
                               try {
                                   assertEquals("camel", service.getKeyword());
                                   assertEquals("BarackObama", service.getTimeline());

                                   service.setKeyword("trump");
                                   service.setTimeline("realDonaldTrump");

                                   Thread.sleep(5000);

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

    @Test
    //just for start server, will never end
    public void startOnly() throws Exception {
        service.start();
    }

}