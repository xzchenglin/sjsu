package dao;

import model.Site;
import org.apache.commons.lang.NotImplementedException;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class SiteImpl extends BasePOJO implements SiteDao {
    @Override
    public Site create(Site o) throws Exception {
        SqlSession s = client.openSession(true);
        s.insert("ns.site.create", o);
        s.close();
        return o;
    }

    @Override
    public Site update(Site o) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public Site find(String key) throws Exception {
        throw new NotImplementedException();
    }

    @Override
    public List<Site> list(String kw) throws Exception {
        SqlSession s = client.openSession(true);
        List<Site> ret = s.selectList("ns.site.list");
        s.close();
        return ret;
    }
}
