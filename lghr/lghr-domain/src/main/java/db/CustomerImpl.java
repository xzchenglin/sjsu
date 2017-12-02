package db;

import model.Customer;
import org.apache.ibatis.session.SqlSession;

import javax.ws.rs.NotSupportedException;
import java.util.List;

/**
 * Created by Lin Cheng
 */
public class CustomerImpl extends BasePOJO implements CustomerDao {
    @Override
    public Customer create(Customer o) throws Exception {
        SqlSession s = client.openSession(true);
        s.insert("ns.customer.create", o);
        s.close();
        return o;
    }

    @Override
    public Customer update(Customer o) throws Exception {
        SqlSession s = client.openSession(true);
        s.update("ns.customer.update", o);
        s.close();
        return o;
    }

    @Override
    public Customer getById(String id) throws Exception {
        int iid = Integer.parseInt(id);
        SqlSession s = client.openSession(true);
        Customer ret = s.selectOne("ns.customer.getById", iid);
        s.close();
        return ret;
    }

    @Override
    public List<Customer> list(String kw) throws Exception {
        SqlSession s = client.openSession(true);
        List<Customer> ret = s.selectList("ns.customer.list");
        s.close();
        return ret;
    }

    @Override
    public void deleteById(String id) throws Exception {
        throw new NotSupportedException();
    }
}
