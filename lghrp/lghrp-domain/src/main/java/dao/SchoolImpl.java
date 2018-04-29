package dao;

import model.School;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by Lin Cheng
 */
public class SchoolImpl extends BasePOJO implements SchoolDao {

    @Override
    public School create(School o) throws Exception{
        SqlSession s = client.openSession(true);
        s.insert("ns.school.create", o);
        s.close();
        return o;
    }

    @Override
    public School update(School o) throws Exception {
        SqlSession s = client.openSession(true);
        s.update("ns.school.update", o);
        s.close();
        return o;
    }

    @Override
    public School getById(Long id) throws Exception{
        SqlSession s = client.openSession(true);
        School o = s.selectOne("ns.school.getById", id);
        s.close();
        return o;
    }

    @Override
    public List<School> list(String kw) throws Exception {
        List<School> ret;
        SqlSession s = client.openSession(true);
        if(StringUtils.isBlank(kw)) {
            ret = s.selectList("ns.school.list");
        } else {
            kw = "%" + kw.toLowerCase() + "%";
            ret = s.selectList("ns.school.list", kw);
        }
        s.close();
        return ret;
    }

    @Override
    public void deleteById(Long id) throws Exception {
        SqlSession s = client.openSession(true);
        s.delete("ns.school.deleteById", id);
        s.close();
    }
}
