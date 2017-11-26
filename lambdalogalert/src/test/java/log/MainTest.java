package log;

import com.amazonaws.services.lambda.runtime.events.KinesisEvent;

import static org.junit.Assert.*;

public class MainTest {
    @org.junit.Test
    public void handleRequest() throws Exception {
//        RedisUtils.hset(123L, "test", "hehe");
        String req = "{\n" +
                "  \"records\": [\n" +
                "    {\n" +
                "      \"eventID\": \"shardId-000000000000:49545115243490985018280067714973144582180062593244200961\",\n" +
                "      \"eventVersion\": \"1.0\",\n" +
                "      \"kinesis\": {\n" +
                "        \"partitionKey\": \"partitionKey-3\",\n" +
//                "        \"data\": \"SGVsbG8sIHRoaXMgaXMgYSB0ZXN0IDEyMy4=\",\n" +
                "        \"kinesisSchemaVersion\": \"1.0\",\n" +
                "        \"sequenceNumber\": \"49545115243490985018280067714973144582180062593244200961\"\n" +
                "      },\n" +
                "      \"invokeIdentityArn\": \"identityarn\",\n" +
                "      \"eventName\": \"aws:kinesis:record\",\n" +
                "      \"eventSourceARN\": \"eventsourcearn\",\n" +
                "      \"eventSource\": \"aws:kinesis\",\n" +
                "      \"awsRegion\": \"us-east-1\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        String str = "    {\n" +
                "      \"eventID\": \"shardId-000000000000:49545115243490985018280067714973144582180062593244200961\",\n" +
                "      \"eventVersion\": \"1.0\",\n" +
                "      \"kinesis\": {\n" +
                "        \"partitionKey\": \"partitionKey-3\",\n" +
//                "        \"data\": \"SGVsbG8sIHRoaXMgaXMgYSB0ZXN0IDEyMy4=\",\n" +
                "        \"kinesisSchemaVersion\": \"1.0\",\n" +
                "        \"sequenceNumber\": \"49545115243490985018280067714973144582180062593244200961\"\n" +
                "      },\n" +
                "      \"invokeIdentityArn\": \"identityarn\",\n" +
                "      \"eventName\": \"aws:kinesis:record\",\n" +
                "      \"eventSourceARN\": \"eventsourcearn\",\n" +
                "      \"eventSource\": \"aws:kinesis\",\n" +
                "      \"awsRegion\": \"us-east-1\"\n" +
                "    }";

        KinesisEvent.KinesisEventRecord r = JsonHelper.fromJson(str, KinesisEvent.KinesisEventRecord.class);
//
//
//        new Main().handleRequest(req, null);
    }

}