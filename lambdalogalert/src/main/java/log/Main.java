package log;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

public class Main implements RequestHandler{
    public Object handleRequest(Object obj, Context context) {
        context.getLogger().log("hello");
        context.getLogger().log(obj + "");
        context.getLogger().log(obj.getClass() + "");
        LinkedHashMap evt = (LinkedHashMap)obj;
        context.getLogger().log(((List)evt.get("Records")).get(0) + "");
        context.getLogger().log(((List)evt.get("Records")).get(0).getClass() + "");
//        KinesisEvent evt = JsonHelper.fromJson(obj+"", KinesisEvent.class);

//        RequestData rd = new RequestData("a", 2);
//        rd.setResponse(401);
//        evt.getRecords().stream().forEach(
//                r -> r.getKinesis().setData(ByteBuffer.wrap(Base64.getEncoder().encode(JsonHelper.toJson(rd).getBytes())))
//        );
        long time = System.currentTimeMillis()/(1000*60*5); //5 mins
        for(LinkedHashMap record : (List<LinkedHashMap>)evt.get("Records")){
            String data = ((LinkedHashMap)record.get("kinesis")).get("data") + "";
            RequestData rd = JsonHelper.fromJson(new String(Base64.getDecoder().decode(data)), RequestData.class);
            int resp = rd.getResponse();
            String respStr = resp + "";
            if(resp > 400) {
                RedisUtils.incr(time, resp + "");
                context.getLogger().log(respStr + ":" + RedisUtils.getNum(time, respStr));
                if(RedisUtils.getNum(time, respStr) > 30){
                    context.getLogger().log("!!!!!!" + respStr + ":" + RedisUtils.getNum(time, respStr));
                    AmazonSNSClient snsClient = new AmazonSNSClient(new AWSCredentials() {
                        @Override
                        public String getAWSAccessKeyId() {
                            return "xxx";
                        }

                        @Override
                        public String getAWSSecretKey() {
                            return "yyy";
                        }
                    });
                    snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
                    PublishRequest publishRequest = new PublishRequest("arn:aws:sns:us-east-1:914381644891:s3update",
                            respStr+ "->" + RedisUtils.getNum(time, respStr));
                    publishRequest.setSubject("Too many failed requests");
                    snsClient.publish(publishRequest);

                    RedisUtils.del(time);
                }
            }
        }

//        ((List)evt.get("Records")).stream().map(LinkedHashMap.class::cast).map(r -> r.getKinesis().getData().array())
//                .map(s -> JsonHelper.fromJson(new String(Base64.getDecoder().decode(s)), RequestData.class)).
//                forEach(d -> {
//                    int resp = (int)d.getResponse();
//                    String respStr = resp + "";
//                    if(resp > 400) {
//                        RedisUtils.incr(time, resp + "");
//                        System.out.println(resp);
//                        System.out.println(RedisUtils.getNum(time, respStr));
////                        context.getLogger().log(respStr + ":" + RedisUtils.getNum(time, respStr));
//                        if(RedisUtils.getNum(time, respStr) > 10){
////                            context.getLogger().log("!!!!!!" + respStr + ":" + RedisUtils.getNum(time, respStr));
//                        }
//                    }
//                });

        return "hello?";
    }

    static public class RequestData {

        public RequestData(String name, int value) {
            this.name = name;
            this.value = value;
        }

        String name;
        int value;
        int response;
        long key;

        public long getKey() {
            return key;
        }

        public void setKey(long key) {
            this.key = key;
        }

        public int getResponse() {
            return response;
        }

        public void setResponse(int response) {
            this.response = response;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
