package domain.s3;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Lin on 2017/10/8.
 */
public class S3ManagerBeanTest {
    S3Manager mgr = new S3ManagerBean();
    @Test
    public void browse() throws Exception {
        assertNotNull(mgr.browse(""));
    }

    @Test
    public void download() throws Exception {
        assertNotNull(mgr.download("1.txt"));
    }

    @Test
    public void delete() throws Exception {
        assertNotNull(mgr.delete("1.txt"));
    }

    @Test
    public void upload() throws Exception {
        assertNotNull(mgr.upload("1.txt"));
    }

    @Test
    public void meta() throws Exception {
        assertNotNull(mgr.meta("1.txt"));
    }

}