package logi.service;

import logi.domain.model.User;

public interface UserService {
    User keygen(User.Role role) throws Exception;
    UserService fakeUser(User currentUser);
}
