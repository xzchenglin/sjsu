package dao;

import model.Group;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by Lin Cheng
 */
public class GroupImpl extends BasePOJO implements GroupDao {
    @Override
    public Group create(Group o) throws Exception {
        SqlSession s = client.openSession();
        s.insert("ns.group.create", o);
        s.commit();
        s.close();
        return o;
    }

    @Override
    public Group update(Group o) throws Exception {
        SqlSession s = client.openSession(true);
        s.update("ns.group.update", o);
        s.close();
        return o;
    }

    @Override
    public Group getById(Long id) throws Exception {
        SqlSession s = client.openSession(true);
        Group ret = s.selectOne("ns.group.getById", id);
        s.close();
        return ret;
    }

    @Override
    public List<Group> list(String sid) throws Exception {
        SqlSession s = client.openSession(true);
        List<Group> ret = s.selectList("ns.group.list", Long.parseLong(sid));
        s.close();
        return ret;
    }

    @Override
    public void deleteById(Long id) throws Exception {
        SqlSession s = client.openSession();
        //TODO del group-user
        s.delete("ns.group.deleteById", id);
        s.commit();
        s.close();
    }
}
