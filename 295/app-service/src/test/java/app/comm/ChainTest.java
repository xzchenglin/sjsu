package app.comm;

import app.domain.bc.Block;
import app.domain.bc.Chain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChainTest {

    @org.junit.Test
    public void append() throws Exception {

        String pk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAi/+4bP7Io44ibPa3qqEIua8V94srBxX4et6KmFY7HBnCo/tM4ToOor+gBiVY5loI0qg3yH/+bUcB7cO8QV9jFJ5Mcp6Ku7YV6o2J498yp/Z2sFCA43qalMY2K6UoUEsxvkN2GeBHvtwyXEW66j0yZCQPEGt6aZev0GtJojYlPesEJiashR2cKfXV9CV6LXLcxWGTplgbiZKZZf1G0rxR/F4jUsWjzLIsPFvTxtKy1/uiOovXRE+rDvHNrvQaz8zD5iXqM1lMqpko+/auEHAO6Y28/eOv5+Hu62hF7Pb8kXaesQAnvKXxRf+TdZdyQRwYdRN5eJeFeNalmdA7PVAkWwIDAQAB";

        Chain chain = new Chain();

        Block b0 = new Block("111", pk, Utils.rsaEnc("111", "/opt/295/keys/sjsu.key"));
        String ret = Utils.md5(JSONHelper.toJson(chain.append(b0).getLast()));

        int n = 10;
        for (int i = 1; i < n; i++) {

//            //modify payload
//            if(i==3){
//                ret += "1";
//            }

            Block b2 = new Block("111", pk, Utils.rsaEnc(ret, "/opt/295/keys/sjsu.key"));
            ret = Utils.md5(JSONHelper.toJson(chain.append(b2).getLast()));

            System.out.println(ret);
        }

        assertEquals(chain.getBlocks().size(), n);

        assertTrue(chain.verify());

        System.out.println(JSONHelper.toJson(chain));
    }

}