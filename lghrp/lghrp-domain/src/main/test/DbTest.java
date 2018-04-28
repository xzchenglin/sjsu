import common.JsonHelper;
import dao.*;
import model.Group;
import model.School;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class DbTest {

    @Test
    public void testSchool() throws Exception {
        SchoolDao manager = new SchoolImpl();

        School o = new School();
        o.setName("name1");

        o = manager.create(o);
        o.setName("name2");
        manager.update(o);

        School ret = manager.getById(o.getId());
        System.out.println(ret.getName());
        System.out.println(ret.getGroups());

        List<School> ps = manager.list("n");
        ps.stream().map(p->p.getName()).forEach(System.out::println);

//        manager.deleteById(o.getId());
    }

    @Test
    public void testCustomer() throws Exception {
    }

    @Test
    public void testGroup() throws Exception {
        GroupDao manager = new GroupImpl();

        Group o = new Group();
        o.setSid(2328L);
        o.setName("aaa");

        o = manager.create(o);

        o.setName("zzz");
        manager.update(o);

        List<Group> os = manager.list("2328");
        os.stream().map(p-> JsonHelper.toJson(p)).forEach(System.out::println);

        o = manager.getById(o.getId());
        System.out.println(o.getName());
        System.out.println(o.getSchool());
        System.out.println(o.getGroupUsers());

//        manager.deleteById(o.getId());
    }

}