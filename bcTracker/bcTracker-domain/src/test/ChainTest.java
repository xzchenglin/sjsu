import helper.JSONHelper;
import helper.Utils;
import bc.Block;
import bc.Chain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChainTest {

    @org.junit.Test
    public void append() throws Exception {

        String pk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsQiCSQRfAlQkKWyIoFWVcxT7yCgZWCi6C7Hob5qVaPej6DQQE4YJ6xEmTPAG6oeclj7G3rAFBTVasVH7Jncb8wlkqoptNmRRnALTvNiItAlMHm+JhqoHGOefxf49qUX+oQ7IthJ6PYJaH7eWNv/dtMPQEKvTl8c3U0jDiG/tlXDcYobuLOvMkjTRsLfzfZQW5j0fqpThnVMLn+512HQueU9FFPCfZJP/tpR6wizfUCIQWqGaIllyI4Tym4EARdoPGjYSa7re701/jcKFLOYFMqfO4HB0ofNBWb3znKGDZzbEf4E2yARK/tVCVVIXX1noNQBZYWhfFmvMR1TR4QXMawIDAQAB";

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

        System.out.println(JSONHelper.toJson(chain));
    }

}