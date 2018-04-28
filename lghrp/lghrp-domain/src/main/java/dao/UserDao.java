package dao;

import model.User;

public interface UserDao extends BaseDao<User> {

    User createAndGetByExId(User cust) throws Exception;
}
