package app.service;

import app.comm.Utils;
import app.domain.model.User;
import app.domain.repository.AddressRepository;
import app.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository ur;
    @Autowired
    private AddressRepository ar;

    //for unit testing
    private User currentUser;
    public UserServiceImpl fakeUser(User currentUser) {
        this.currentUser = currentUser;
        return this;
    }

    @Transactional
    @Override
    public User keygen(User.Role role) throws Exception {

        if(SecurityContextHolder.getContext().getAuthentication() != null) {
            currentUser = ur.findByName(SecurityContextHolder.getContext().getAuthentication().getName())
                    .orElseThrow(() -> new RuntimeException("Invalid user."));
        }

        String pk = Utils.generateKeypair(currentUser.getName());
        currentUser.setPubkey(pk);
        currentUser.setRole(role);
        ur.save(currentUser);

        return currentUser;
    }
}
