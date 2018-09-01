package logi.controller;

import logi.domain.model.User;
import logi.domain.repository.UserRepository;
import logi.domain.service.UserService;
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

    @PostMapping(value = "/keygen")
    public User register(@RequestParam(value="role") User.Role role) throws Exception {
        return us.keygen(role);
    }

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

    @GetMapping("/find")
    public User find(@RequestParam(value="id") Long id) {
        User u = ur.findById(id).orElseThrow(() -> new EntityNotFoundException(id + ""));
        return u;
    }
}
