package app.controller;

import app.domain.model.User;
import app.domain.repository.UserRepository;
import app.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    static Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private UserService us;
    @Autowired
    private UserRepository ur;

    /***
     * assign a {@link app.domain.model.User.Role} to user and generate key pair fot them
     *
     * @param role {@link app.domain.model.User.Role}
     * @return update user
     */
    @GetMapping(value = "/keygen")
    public User keygen(@RequestParam(value="role") User.Role role) throws Exception {
        return us.keygen(role);
    }

    /***
     *
     * @param detailed include dependencies like adresses or not
     * @return list of users
     */
    @GetMapping("/list")
    public List<User> list(@RequestParam(value="detailed") boolean detailed) {
        List<User> is = new ArrayList<>();
        if(detailed){
            is = ur.findAllEager();
        } else {
            ur.findAll().forEach(is::add);
        }
        return is;
    }

    /**
     *
     * @param id
     * @return {@link User}
     */
    @GetMapping("/find")
    public User find(@RequestParam(value="id") Long id) {
        User u = ur.findById(id).orElseThrow(() -> new EntityNotFoundException(id + ""));
        return u;
    }
}
