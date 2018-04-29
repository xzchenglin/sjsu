import common.JsonHelper;
import dao.*;
import helper.DynamoHelper;
import model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
        UserDao manager = new UserImpl();

        User o = new User();
        o.setName("uuu");
        o.setExternalid("aaa");
        o.setMail("a@b.com");
        o.setPhone("123");

        List<UserSchool> usl = new ArrayList<>();
        usl.add(new UserSchool(2008, "CS", "master", 2322L));
        usl.add(new UserSchool(2010, "EE", "master", 2318L));
        o.setUserSchools(usl);

        o = manager.create(o);

        o.setGender("female");
        o.getUserSchools().get(0).setYear(2009);
        manager.update(o);

        List<User> os = manager.list(null);
        os.stream().map(p-> JsonHelper.toJson(p)).forEach(System.out::println);

        UserGroup ug = new UserGroup(true, 2339L, o.getId());
        UserGroup ug2 = new UserGroup(true, 2352L, o.getId());
        manager.joinGroup(ug);
        manager.joinGroup(ug2);

        o = manager.getById(o.getId());
        System.out.println(o.getName());

//        manager.deleteById(o.getId());
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

        o = manager.getById(2352L);
        System.out.println(o.getName());
        System.out.println(o.getSchool());
        System.out.println(o.getGroupUsers());

//        manager.deleteById(o.getId());
    }

    @Test
    public void testDynamo() throws Exception{
//        Post p = new Post(1L, 2L, System.currentTimeMillis(), "hahaha");
//        DynamoHelper.write(p);

        DynamoHelper.search(2L).stream().forEach(System.out::println);
    }

}