import common.JsonHelper;
import db.*;
import model.Customer;
import model.Order;
import model.OrderDetail;
import model.Product;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class DbTest {

    @Test
    public void testProduct() throws Exception {
        ProductDao manager = new ProductImpl();

        String key = "key" + new Random().nextInt();
        Product o = new Product();
        o.setProductCode(key);
        o.setBuyPrice(1.1);
        o.setMSRP(2.2);
        o.setProductDescription("desc");
        o.setProductName("name1");

        manager.create(o);
        o.setProductDescription("vvv");
        manager.update(o);

        Product ret = manager.getById(key);
        System.out.println(ret.getProductDescription());

        List<Product> ps = manager.list("z");
        ps.stream().map(p->p.getProductName()).forEach(System.out::println);

        manager.deleteById(key);
    }

    @Test
    public void testCustomer() throws Exception {
        CustomerDao manager = new CustomerImpl();

        int key = Math.abs(new Random().nextInt());
        Customer o = new Customer();
        o.setCity("aaa");
        o.setCustomerName("bbb");
        o.setCreditLimit(1.1);
//        o.setCustomerNumber(key);

        manager.create(o);
        System.out.println(o.getCustomerNumber());

        o.setCreditLimit(2.2);
        Customer pp = manager.update(o);
        System.out.println(JsonHelper.toJson(pp));

        Customer c = manager.getById(key+"");
        c = manager.getById("103");
        System.out.println(JsonHelper.toJson(c));

        List<Customer> ps = manager.list(null);
        ps.stream().map(p-> JsonHelper.toJson(p)).forEach(System.out::println);
    }

    @Test
    public void testOrder() throws Exception {
        OrderDao manager = new OrderImpl();

        int key = Math.abs(new Random().nextInt());
        Order o = new Order();
//        Customer c = new Customer();
//        c.setCustomerNumber(103);
//        o.setCustomer(c);
        o.setCustomerNumber(103);
//        o.setOrderNumber(key);
        o.setOrderDate(new Date());
        o.setStatus("aaa");

        OrderDetail od = new OrderDetail();
        od.setOrderNumber(key);
        od.setPriceEach(1.2);
        od.setProductCode("abc1");
        od.setQuantityOrdered(2);
        o.addOrderDetail(od);

        OrderDetail od2 = new OrderDetail();
        od2.setOrderNumber(key);
        od2.setPriceEach(1.22);
        od2.setProductCode("abc");
        od2.setQuantityOrdered(3);
        o.addOrderDetail(od2);

        manager.create(o);

        o.setStatus("zzz");
        manager.update(o);

        List<Order> os = manager.list("103");
        os.stream().map(p-> JsonHelper.toJson(p)).forEach(System.out::println);

        Order oo = manager.getById(key + "");
        System.out.println(oo.getStatus());
        oo = manager.getById("10345");
        System.out.println(JsonHelper.toJson(oo));

        manager.deleteById(key + "");
    }

}