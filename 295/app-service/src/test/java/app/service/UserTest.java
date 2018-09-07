package app.service;

import app.domain.model.Address;
import app.domain.model.User;
import app.domain.repository.AddressRepository;
import app.domain.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private UserService us;
    @Autowired
    private UserRepository ur;
    @Autowired
    private AddressRepository ar;
    @Autowired
    ApplicationContext ctx;

    @Test
    public void keyGen() throws Exception {
        us.fakeUser(ur.findByName("aaa").get()).keygen(User.Role.Driver);
        us.fakeUser(ur.findByName("bbb").get()).keygen(User.Role.Driver);
        us.fakeUser(ur.findByName("ccc").get()).keygen(User.Role.Driver);
    }

    @Test
    public void addUser() throws Exception {
        User u = new User();
        u.setName("ddd");
        u.setPwd("ppp");

        Address a2 = new Address();
        a2.setCity("Santa Clara");
        a2.setZip("8888");
        List<Address> as = new ArrayList<>();
        a2.setUser(u);
        as.add(a2);

        u.setAddresses(as);
        u = ur.save(u);
        u = ur.findById(u.getId()).get();
        u = u;
    }

    @Test
    public void finaUser() throws Exception {
        User u = ur.findById(86L).get();
        u.toString();
    }
}
