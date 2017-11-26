package log;

import com.amazonaws.services.kinesis.model.PutRecordsRequestEntry;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class KinesisManagerTest {
//    @Test
    public void read() throws Exception {
        KinesisManager.read("http-res");
    }

//    @Test
    public void read2() throws Exception {
        KinesisManager.read("activity");
    }

//    @Test
    public void write() throws Exception {
        while (true) {
            List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();
            int i = Math.abs((int) Math.ceil(new Random().nextInt() / 1000));
            int ii = i + 3;

            //test data
            for (; i < ii; i++) {
                TestData data = new TestData("test" + i, i);
                data.setResponse(400 + ii - i);
                PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
                putRecordsRequestEntry.setData(ByteBuffer.wrap(JsonHelper.toJson(data).getBytes()));
                putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
                putRecordsRequestEntryList.add(putRecordsRequestEntry);
            }
            KinesisManager.write("http-res", putRecordsRequestEntryList);

            try {
                Thread.sleep(6000);
            }
            catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

//    @Test
    public void write2() throws Exception {
        while (true) {
            List<PutRecordsRequestEntry> putRecordsRequestEntryList = new ArrayList<>();

            //test data
            int seed = Math.abs(new Random().nextInt());
            int fi = seed % KinesisManagerTest.TestData2.foods.length;
            int ui = seed % KinesisManagerTest.TestData2.units.length;

            TestData2 data = new TestData2();
            data.setCount(seed % 300);
            data.setFood(KinesisManagerTest.TestData2.foods[fi]);
            data.setUnit(KinesisManagerTest.TestData2.foods[ui]);
            data.setMealId(seed % 5);
            data.setUid(seed % 3 + 100);
            PutRecordsRequestEntry putRecordsRequestEntry = new PutRecordsRequestEntry();
            putRecordsRequestEntry.setData(ByteBuffer.wrap(JsonHelper.toJson(data).getBytes()));
            putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", seed));
            putRecordsRequestEntryList.add(putRecordsRequestEntry);

            KinesisManager.write("activity", putRecordsRequestEntryList);

            try {
                Thread.sleep(6000);
            }
            catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
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

    static public class TestData2{

        public static String[]foods = ("banana" +
                " Milk" +
                " rice" +
                " Egg" +
                " PASTA SALAD" +
                " milk" +
                " potato").split(" ");
        public static String[]units = ("gm" +
                " ml" +
                " oz").split(" ");

        int uid;
        int mealId;
        int count;
        String food;
        String unit;
        int time;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getMealId() {
            return mealId;
        }

        public void setMealId(int mealId) {
            this.mealId = mealId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getFood() {
            return food;
        }

        public void setFood(String food) {
            this.food = food;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}