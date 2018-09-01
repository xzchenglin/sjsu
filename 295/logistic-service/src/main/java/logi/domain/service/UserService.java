package logi.domain.service;

import logi.domain.model.User;

public interface UserService {
    User keygen(User.Role role) throws Exception;

    UserService user(User currentUser);
}
