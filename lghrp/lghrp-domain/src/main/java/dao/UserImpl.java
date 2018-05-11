package dao;

import model.School;
import model.User;
import model.UserGroup;
import model.UserSchool;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;

import javax.ws.rs.NotSupportedException;
import java.util.List;

/**
 * Created by Lin Cheng
 */
public class UserImpl extends BasePOJO implements UserDao {

    @Override
    public User create(User o) throws Exception {
        SqlSession s = client.openSession();
        try {
            s.insert("ns.user.create", o);
            if (o.getUserSchools() != null) {
                for (UserSchool us : o.getUserSchools()) {
                    us.setUid(o.getId());
                    List<School> schools = s.selectList("ns.user.getSchool", us);
                    if(schools == null || schools.size()==0) {
                        s.insert("ns.user.addSchool", us);
                    } else {
                        s.update("ns.user.updateSchool", us);
                    }
                }
            }
            s.commit();
            return o;
        } finally {
            s.close();
        }
    }

    @Override
    public User update(User o) throws Exception {
        SqlSession s = client.openSession();
        try {
            s.update("ns.user.update", o);
            if (o.getUserSchools() != null) {
                for (UserSchool us : o.getUserSchools()) {
                    us.setUid(o.getId());
                    List<School> schools = s.selectList("ns.user.getSchool", us);
                    if(schools == null || schools.size()==0) {
                        s.insert("ns.user.addSchool", us);
                    } else {
                        s.update("ns.user.updateSchool", us);
                    }
                }
            }
            s.commit();
            return o;
        } finally {
            s.close();
        }
    }

    @Override
    public User getById(Long id) throws Exception {
        SqlSession s = client.openSession(true);
        try {
            User ret = s.selectOne("ns.user.getById", id);
            return ret;
        } finally {
            s.close();
        }
    }

    @Override
    public User createAndGetByExId(User cust) throws Exception {

        SqlSession s = client.openSession(true);

        try {
            List<User> ret = s.selectList("ns.user.getByExid", cust.getExternalid());
            if (ret == null || ret.size() == 0) {
                ret = s.selectList("ns.user.getByMail", cust.getMail());
                if (ret == null || ret.size() == 0) {
                    s.insert("ns.user.create", cust);
                    return cust;
                } else {
                    throw new Exception("Email already exist.");
                }
            }
            return ret.get(0);
        }
        finally {
            s.close();
        }
    }

    @Override
    public User getByFid(String fid) throws Exception {

        SqlSession s = client.openSession(true);

        try {
            List<User> ret = s.selectList("ns.user.getByFid", fid);
            if (ret == null || ret.size() == 0) {
                return null;
            }
            return ret.get(0);
        }
        finally {
            s.close();
        }
    }

    @Override
    public UserGroup joinGroup(UserGroup ug) {
        SqlSession s = client.openSession(true);
        try {
            List<UserGroup> gs = s.selectList("ns.user.getGroup", ug);
            if (gs == null || gs.size() == 0) {
                s.insert("ns.user.addGroup", ug);
            }
            return ug;
        } finally {
            s.close();
        }
    }

    @Override
    public UserGroup exitGroup(UserGroup ug) {
        SqlSession s = client.openSession(true);
        try {
            s.delete("ns.user.delGroup", ug);
            return ug;
        } finally {
            s.close();
        }
    }

    @Override
    public List<User> list(String kw) throws Exception {
        SqlSession s = client.openSession(true);
        List<User> ret;
        if(StringUtils.isBlank(kw)) {
            ret = s.selectList("ns.user.list");
        } else {
            kw = "%" + kw.toLowerCase() + "%";
            ret = s.selectList("ns.user.list", kw);
        }
        s.close();
        return ret;
    }

    @Override
    public void deleteById(Long id) throws Exception {
        throw new NotSupportedException();
    }
}
