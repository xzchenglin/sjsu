package logi.service;

import logi.comm.Utils;
import logi.domain.model.User;
import logi.domain.repository.AddressRepository;
import logi.domain.repository.UserRepository;
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
