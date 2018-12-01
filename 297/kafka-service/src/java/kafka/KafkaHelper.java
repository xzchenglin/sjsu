package kafka;

import kafka.consumer.ConsumerCreator;
import kafka.producer.ProducerCreator;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import service.Utils;

import java.io.*;
import java.util.concurrent.ExecutionException;

public class KafkaHelper {
    public static String read(String topic) {
        String ret = "";
        try {
            ret = Utils.readRawContentFromFile("/opt/297/" + topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String write(String topic, String txt) {
        Producer<Long, String> producer = ProducerCreator.createProducer();

        final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(topic, txt);
        Long index = System.currentTimeMillis();
        try {
            RecordMetadata metadata = producer.send(record).get();
            String ret = "Record sent with key " + index + " to partition " + metadata.partition()
                    + " with offset " + metadata.offset();
            System.out.println(ret);
            return ret;
        } catch (ExecutionException e) {
            System.out.println("Error in sending record");
            System.out.println(e);
        } catch (InterruptedException e) {
            System.out.println("Error in sending record");
            System.out.println(e);
        }
        return null;
    }

    public static void startListen(String topic) {
            Consumer<Long, String> consumer = null;
        try {
            consumer = ConsumerCreator.createConsumer(topic);
            String path = "/opt/297/" + topic;
            File f = new File(path);
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                ConsumerRecords<Long, String> consumerRecords = consumer.poll(100);

                if (consumerRecords.count() > 0) {
                    try (FileWriter fw = new FileWriter(path, true);
                         BufferedWriter bw = new BufferedWriter(fw);
                         PrintWriter out = new PrintWriter(bw)) {
                        consumerRecords.forEach(record -> {
                            System.out.println("Record Key " + record.key());
                            System.out.println("Record value " + record.value());
                            System.out.println("Record partition " + record.partition());
                            System.out.println("Record offset " + record.offset());

                            out.println(record.value());

                        });
                        consumer.commitAsync();
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
            }
        }finally {
            consumer.close();
        }
    }

}
