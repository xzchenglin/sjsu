package app.controller;

import app.domain.model.User;
import app.domain.repository.UserRepository;
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

    /***
     * (post) register new users
     *
     * @param user {@link User}
     * @return registered user
     */
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        if (ur.findByName(user.getName()).isPresent()) {
            throw new RuntimeException("User is already existing.");
        }
        User ret = ur.save(user);
        return ret;
    }

}
