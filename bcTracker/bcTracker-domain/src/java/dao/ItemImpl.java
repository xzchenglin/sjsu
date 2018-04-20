package dao;

import model.Item;
import org.apache.commons.lang.NotImplementedException;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ItemImpl extends BasePOJO implements ItemDao {
    @Override
    public Item create(Item o) throws Exception {
        SqlSession s = client.openSession(true);
        s.insert("ns.item.create", o);
        s.close();
        return o;
    }

    @Override
    public Item update(Item o) throws Exception {
        SqlSession s = client.openSession(true);
        s.update("ns.item.update", o);
        s.close();
        return o;
    }

    @Override
    public Item find(String key) throws Exception {
        SqlSession s = client.openSession(true);
        Item ret = s.selectOne("ns.item.getById", key);
        s.close();
        return ret;
    }

    @Override
    public List<Item> findByPk(String pk) throws Exception {
        SqlSession s = client.openSession(true);
        List<Item> ret = s.selectList("ns.item.getByPk", pk);
        s.close();
        return ret;
    }

    @Override
    public List<Item> list(String kw) throws Exception {
        throw new NotImplementedException();
    }
}
