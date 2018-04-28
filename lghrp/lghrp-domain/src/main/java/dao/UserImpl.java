package dao;

import model.User;
import org.apache.ibatis.session.SqlSession;

import javax.ws.rs.NotSupportedException;
import java.util.List;

/**
 * Created by Lin Cheng
 */
public class UserImpl extends BasePOJO implements UserDao {
    @Override
    public User create(User o) throws Exception {
        SqlSession s = client.openSession(true);
        s.insert("ns.customer.create", o);
        s.close();
        return o;
    }

    @Override
    public User update(User o) throws Exception {
        SqlSession s = client.openSession(true);
        s.update("ns.customer.update", o);
        s.close();
        return o;
    }

    @Override
    public User getById(Long id) throws Exception {
        SqlSession s = client.openSession(true);
        User ret = s.selectOne("ns.customer.getById", id);
        s.close();
        return ret;
    }

    @Override
    public User createAndGetByExId(User cust) throws Exception {

        SqlSession s = client.openSession(true);
        User ret = s.selectOne("ns.customer.getByExid", cust.getMail());
        if(ret == null){
            s.insert("ns.customer.create", cust);
            ret = cust;
        }

        s.close();
        return ret;
    }

    @Override
    public List<User> list(String kw) throws Exception {
        SqlSession s = client.openSession(true);
        List<User> ret = s.selectList("ns.customer.list");
        s.close();
        return ret;
    }

    @Override
    public void deleteById(Long id) throws Exception {
        throw new NotSupportedException();
    }
}
