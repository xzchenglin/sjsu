package dao;

import model.User;
import model.UserGroup;

public interface UserDao extends BaseDao<User> {

    User createAndGetByExId(User cust) throws Exception;
    UserGroup joinGroup(UserGroup ug);
}
