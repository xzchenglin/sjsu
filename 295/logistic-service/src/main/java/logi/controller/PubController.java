package logi.controller;

import logi.domain.model.User;
import logi.domain.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pub")
public class PubController {

    static Log logger = LogFactory.getLog(PubController.class);

    @Autowired
    private UserRepository ur;

    @PostMapping("/register")
    public User createNewUser(@RequestBody User user) {
        if (ur.findByName(user.getName()) != null) {
            throw new RuntimeException("User is already existing.");
        }
        User ret = ur.save(user);
        return ret;
    }

}
