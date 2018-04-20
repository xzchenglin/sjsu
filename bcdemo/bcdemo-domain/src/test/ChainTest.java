import helper.JSONHelper;
import helper.Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChainTest {

    @org.junit.Test
    public void append() throws Exception {

        String pk = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjXqjE4tIbjyKSAynXQlttRLt1Gr0/ugATJIgnDHz9GWfrGqa+J4wGgEHhUMMSDzfB7ZsOW8Fk8+7KE+9M1049UeLLgIbbRhz2NJAsJVxr+dm6byAGCRINKFM+EBVwsDivrq5cCgfjjKosFeVNG9nG/jSMqqVByd8GuW7eiUUtWI0Nr9wOLU7gqeJwlWrvf4byNpuII2zc79o6iyR8h7VUXbw5yyLuiAljcsuKj/rVwUGOzKLq6Yu5X+L5yY+eFrfrBeRa1YNz643Ku38nRpmx4Mjbe+L77ao20PbyAOKKu4vsIa2U2H24P2HeanRKeJKNRNYzxceXtvOLiVgrlh/CQIDAQAB";

        Chain chain = new Chain();

        Block b0 = new Block("111", pk, Utils.rsaEnc("111", "/opt/sjsu.key"));
        String ret = chain.append(b0);

        int n = 10;
        for (int i = 1; i < n; i++) {

//            //modify payload
//            if(i==3){
//                ret += "1";
//            }

            Block b2 = new Block("111", pk, Utils.rsaEnc(ret, "/opt/sjsu.key"));
            ret = chain.append(b2);

            System.out.println(ret);
        }

        assertEquals(chain.getBlocks().size(), n);

        System.out.println(JSONHelper.toJson(chain));
    }

}