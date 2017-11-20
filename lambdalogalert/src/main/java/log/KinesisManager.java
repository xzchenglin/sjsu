package log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Lin Cheng
 */
public class KinesisManager {

    static AmazonKinesis kinesisClient;

    static {
        AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();

        clientBuilder.setRegion("us-east-1");
        clientBuilder.setCredentials(new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return new BasicAWSCredentials("xxx", "yyy");
            }

            @Override
            public void refresh() {

            }
        });
        clientBuilder.setClientConfiguration(new ClientConfiguration());

        kinesisClient = clientBuilder.build();
    }

    public static void write(){

        while (true) {
            PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
            putRecordsRequest.setStreamName("test");
            List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();
            int i = Math.abs((int)Math.ceil(new Random().nextInt()/1000));
            int ii = i + 3;

            for (; i < ii; i++) {
                TestData data = new TestData("test" + i, i);
                data.setResponse(400 + ii - i);
                PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
                putRecordsRequestEntry.setData(ByteBuffer.wrap(JsonHelper.toJson(data).getBytes()));
                putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
                putRecordsRequestEntryList.add(putRecordsRequestEntry);
            }

            putRecordsRequest.setRecords(putRecordsRequestEntryList);
            PutRecordsResult putRecordsResult = kinesisClient.putRecords(putRecordsRequest);
            putRecordsResult.getRecords().stream().forEach(System.out::println);

            try {
                Thread.sleep(3000);
            }
            catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }

        }
    }

    public static void read(){
        String shardIterator;
        GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest();
        getShardIteratorRequest.setStreamName("test");
        getShardIteratorRequest.setShardId("shardId-000000000000");
        getShardIteratorRequest.setShardIteratorType("TRIM_HORIZON");

        GetShardIteratorResult getShardIteratorResult = kinesisClient.getShardIterator(getShardIteratorRequest);
        shardIterator = getShardIteratorResult.getShardIterator();

        while (true) {
            GetRecordsRequest getRecordsRequest = new GetRecordsRequest();
            getRecordsRequest.setShardIterator(shardIterator);
            getRecordsRequest.setLimit(100);

            GetRecordsResult result = kinesisClient.getRecords(getRecordsRequest);

            result.getRecords().stream().map(v->new String(v.getData().array(), StandardCharsets.UTF_8)).forEach(System.out::println);

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }

            shardIterator = result.getNextShardIterator();
        }
    }

    static public class TestData{

        public TestData(String name, int value) {
            this.name = name;
            this.value = value;
        }

        String name;
        int value;
        int response;

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
