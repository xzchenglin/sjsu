package app.service;

import app.domain.model.User;

public interface UserService {
    User keygen(User.Role role) throws Exception;
    UserService fakeUser(User currentUser);
}
