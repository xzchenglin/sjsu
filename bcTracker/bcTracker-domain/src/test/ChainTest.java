import helper.JSONHelper;
import helper.Utils;
import bc.Block;
import bc.Chain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ChainTest {

    @org.junit.Test
    public void append() throws Exception {

        String pk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtUorNTGKteO9BDJqcCXXyGP9kJMUrzwUbhFXcxkysI/7dBeKFKz8u5sFJtQHbJLvNjG4CZre+G3wmLefmH0yu4Y5UT7uRzbH0g8qEaxcvgx+6icfStlY+fPetDs2OyJPv3vUa2spFK7U7drNuopmaUPuqg41EMnbomtkG4bIr+X9aJQQh81/KeAw6TICrIeJJujrjS2mSd3Lntw3mym2ASSfbAStbzuZ7ZwwFCt/UBauoRG/LP99QqXFQQwC+GsGKZ8yeKVIU8SIsKrgLIGz0ez8pUnMtqZDSl5AX6ctv789kPRxmYhx78hcMPnRa1EDHn4FL/Kmi97Wl9ww5E52EwIDAQAB";

        Chain chain = new Chain();

        Block b0 = new Block("111", pk, Utils.rsaEnc("111", "/opt/bc/sjsu.key"));
        String ret = Utils.md5(JSONHelper.toJson(chain.append(b0).getLast()));

        int n = 10;
        for (int i = 1; i < n; i++) {

//            //modify payload
//            if(i==3){
//                ret += "1";
//            }

            Block b2 = new Block("111", pk, Utils.rsaEnc(ret, "/opt/bc/sjsu.key"));
            ret = Utils.md5(JSONHelper.toJson(chain.append(b2).getLast()));

            System.out.println(ret);
        }

        assertEquals(chain.getBlocks().size(), n);

        assertTrue(chain.verify());

        System.out.println(JSONHelper.toJson(chain));
    }

}