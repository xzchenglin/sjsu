import dragon.dao.Daos;
import dragon.dao.GroupDao;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GroupBeanTest {

    GroupDao dao = Daos.get(GroupDao.class);

    @Test
    public void getGroups() throws Exception {
        List ret = dao.getGroups("xzchenglin@gmail.com");
        System.out.println(ret);
    }

}