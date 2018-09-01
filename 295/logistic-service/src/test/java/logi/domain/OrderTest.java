package logi.domain;

import javafx.util.Pair;
import logi.comm.Utils;
import logi.domain.model.Address;
import logi.domain.model.Order;
import logi.domain.model.User;
import logi.domain.repository.AddressRepository;
import logi.domain.repository.OrderRepository;
import logi.domain.repository.UserRepository;
import logi.domain.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderService os;
    @Autowired
    private OrderRepository or;
    @Autowired
    private AddressRepository ar;
    @Autowired
    private UserRepository ur;
    @Autowired
    ApplicationContext ctx;

    static Order gOrder;

    String sender = "aaa";
    String driver1= "bbb";
    String driver2= "ccc";

    @Test
    public void end2endOrder() throws Exception {
        placeOrder();
        takeOrder();//take the order online
        pickOrder();//pickup the item
        pickOrder2();//hand to next driver
        deliverOrder();

        gOrder = or.findById(gOrder.getId()).get();
        System.out.println(gOrder);
        os.retrieveHistory(gOrder).stream().map(u->u.toString()).forEach(System.out::println);
    }

    @Test
    public void placeOrder() throws Exception {

        Order order = new Order().ctx(ctx);
        order.setName("ooo");
        order.setReceiverPhone("12345");
        Address a1 = new Address();
        a1.setZip("99999");
        a1.setState("CA");
        a1.setAddr("2222, abc st");
        order.setDestAddr(a1);

        User u = ur.findByName(sender);

        //sender's pub key
        order.setNextPubkey(u.getPubkey());
        //sender's pri key
        order.setPayload(Utils.rsaEnc(order.getName(), "/opt/295/keys/" + sender + ".key"));

        try {
            order = os.user(u).place(order);
            System.out.println(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gOrder = order.ctx(ctx);
    }

    @Test
    public void takeOrder() throws Exception {

        Order order = gOrder == null ? or.findById(64L).get().ctx(ctx) : gOrder;

        try {
            order = os.user(ur.findByName(driver1)).take(order);
            System.out.println(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gOrder = order.ctx(ctx);
    }

    @Test
    public void deliverOrder() throws Exception {

        Order order = gOrder == null ? or.findById(64L).get().ctx(ctx) : gOrder;

        try {
            order = os.deliver(order);
            System.out.println(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gOrder = order.ctx(ctx);
    }

    @Test
    public void pickOrder() throws Exception {

        Order order = gOrder == null ? or.findById(64L).get().ctx(ctx) : gOrder;

        User u = ur.findByName(driver1);

        //driver's pub key
        order.setNextPubkey(u.getPubkey());
        //sender's pri key
        order.setPayload(Utils.rsaEnc(order.getHash(), "/opt/295/keys/" + sender + ".key"));

        try {
            order = os.handover(order);
            System.out.println(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gOrder = order.ctx(ctx);
    }

    @Test
    public void pickOrder2() throws Exception {

        Order order = gOrder == null ? or.findById(64L).get().ctx(ctx) : gOrder;

        User u = ur.findByName(driver2);

        //next driver's pub key
        order.setNextPubkey(u.getPubkey());
        //current driver's pri key
        order.setPayload(Utils.rsaEnc(order.getHash(), "/opt/295/keys/" + driver1 + ".key"));

        try {
            order = os.handover(order);
            System.out.println(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
        gOrder = order.ctx(ctx);
    }

    @Test
    public void finaOrder() throws Exception {
        Order o = or.findById(139L).get();
        o.toString();
    }

    @Test
    public void finaOrderByKey() throws Exception {
        Collection<Order> o = or.findByRaw(Collections.singletonList(new Pair("o.next_pubkey", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuh0v+SnT4VpP0CMVJF/uGvpnbBFiVecqzz/zD0//04xxaum/0hjgqkXVApr6eselKSvkcmBCy+Ql/pZM3QoFesrszMEB3GP9gHfC6DazhbXq10AWLQoKm0p+yjy3PpUAK6U3RLbkhcZSEiJhBT3LeqjnVzMn45hF6X/GCSGYzqncULygo4RRJ+5G9A4IcjOCYSDKmwHXYDXFgnRnNulewfVcUe3EqXMQEirYt5UsLk1KjfQYKubIZY+IslPYj5nKJ60ooBQUU3hu2gBiMIIRCDA9m0B2Md+ab+X38U2YZaa9tzy3U0OWc4h9uqZ3Q4NqH8JHzM7Tk6EdEnILBTOOhwIDAQAB")));
        o.toString();
    }


}
