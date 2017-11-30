import service.RestService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/***
 * Created by Lin Cheng on 2017/9/25.
 */
public class RestServiceTest {

    RestService service = RestService.instance("A3r6vuJNqxRxWy7vvQYKvN3zY",
            "FoZQyC6zyrrRb7FUFbei3q0gXOCHISbExU0UADHWIFDUFol3c4",
            "42130867-xhH261CGI62un52HGm66Iks2Xlw7OYaJijm3ucwVG",
            "0TN0VWs6CFsYKFDHxKtZnmbIgaLMuIxI8Itl2vsHWyjwl");

    @Test
    //just for start server, will never end
    public void startOnly() throws Exception {
        service.start();
    }

}