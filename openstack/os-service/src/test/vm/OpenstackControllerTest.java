package vm;

import org.junit.Test;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.identity.v3.Token;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Lin Cheng
 *
 */
public class OpenstackControllerTest {
    @Test
    public void authenticate() throws Exception {
        Token t = OpenstackController.instance().authenticate();
        assertNotNull(t);
        System.out.println(t.getId());
    }

    @Test
    public void getServers() throws Exception {
        List ss =  OpenstackController.instance().getServers();
        assertNotNull(ss);
        System.out.println(ss.get(0));
    }

    @Test
    public void getFlavors() throws Exception {
        List ss =  OpenstackController.instance().getFlavors();
        assertNotNull(ss);
        System.out.println(ss.get(0));
    }

    @Test
    public void getImages() throws Exception {
        List ss =  OpenstackController.instance().getImages();
        assertNotNull(ss);
        System.out.println(ss.get(0));
    }

    @Test
    public void getNetworks() throws Exception {
        List ss =  OpenstackController.instance().getNetworks();
        assertNotNull(ss);
        System.out.println(ss.get(1));
    }

    @Test
    public void getSubnets() throws Exception {
        List ss =  OpenstackController.instance().getSubnets();
        assertNotNull(ss);
        System.out.println(ss.get(0));
    }

    @Test
    public void opServer() throws Exception {
        int code = OpenstackController.instance().opServer("c21f4220-4de7-4612-ae6e-f18aa15bf159", "STOP");
        assertEquals(200, code);
    }

    @Test
    public void createAndDelServer() throws Exception {
        Server server = OpenstackController.instance().createServer("s1", "594ead10-364d-460e-8d41-2fb0d41b5529",
                "37eb53e9-4f6d-4bc4-842c-9dce70b3c226", "89e52636-d0a9-481b-be08-43a477f20803");
        assertNotNull(server);
        System.out.println(server);

        int code = OpenstackController.instance().delServer(server.getId());
        assertEquals(200, code);
    }

}