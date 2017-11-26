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
//        clientBuilder.setEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("kinesis.us-east-1.amazonaws.com","us-east-1"));
        clientBuilder.setCredentials(new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
//                return new BasicAWSCredentials("xxx", "xxx");
                return new BasicAWSCredentials("xxx", "xxx");
            }

            @Override
            public void refresh() {

            }
        });
        clientBuilder.setClientConfiguration(new ClientConfiguration());

        kinesisClient = clientBuilder.build();
    }

    public static void write(String name, List<PutRecordsRequestEntry> putRecordsRequestEntryList){

        PutRecordsRequest putRecordsRequest = new PutRecordsRequest();
        putRecordsRequest.setStreamName(name);

        putRecordsRequest.setRecords(putRecordsRequestEntryList);
        PutRecordsResult putRecordsResult = kinesisClient.putRecords(putRecordsRequest);
        putRecordsResult.getRecords().stream().forEach(System.out::println);
    }

    public static void read(String name){
        String shardIterator;
        GetShardIteratorRequest getShardIteratorRequest = new GetShardIteratorRequest();
        getShardIteratorRequest.setStreamName(name);
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

}
