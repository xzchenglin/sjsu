package db;

import model.Order;
import model.OrderDetail;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by Lin Cheng
 */
public class OrderImpl extends BasePOJO implements OrderDao {
    @Override
    public Order create(Order o) throws Exception {
        SqlSession s = client.openSession();
        s.insert("ns.order.create", o);
        o.getOrderDetails().stream().forEach(d->d.setOrderNumber(o.getOrderNumber()));
        s.insert("ns.order.createDetails", o.getOrderDetails());
        s.commit();
        s.close();
        return o;
    }

    @Override
    public Order update(Order o) throws Exception {
        SqlSession s = client.openSession(true);
        s.update("ns.order.update", o);
        s.close();
        return o;
    }

    @Override
    public Order getById(String id) throws Exception {
        int iid = Integer.parseInt(id);
        SqlSession s = client.openSession(true);
        Order ret = s.selectOne("ns.order.getById", iid);
        s.close();
        return ret;
    }

    @Override
    public List<Order> list(String cid) throws Exception {
        SqlSession s = client.openSession(true);
        List<Order> ret = s.selectList("ns.order.list", Integer.parseInt(cid));
        s.close();
        return ret;
    }

    @Override
    public void deleteById(String id) throws Exception {
        SqlSession s = client.openSession();
        s.delete("ns.order.deleteDetails", Integer.parseInt(id));
        s.delete("ns.order.deleteById", Integer.parseInt(id));
        s.commit();
        s.close();
    }
}
