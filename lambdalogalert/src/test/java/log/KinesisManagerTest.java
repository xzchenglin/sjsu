package log;

import org.junit.Test;

import static org.junit.Assert.*;

public class KinesisManagerTest {
    @Test
    public void read() throws Exception {
        KinesisManager.read();
    }

    @Test
    public void write() throws Exception {
        KinesisManager.write();
    }

}