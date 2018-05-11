package dao;

import model.User;
import model.UserGroup;

public interface UserDao extends BaseDao<User> {

    User createAndGetByExId(User cust) throws Exception;
    User getByFid(String fid) throws Exception;
    UserGroup joinGroup(UserGroup ug);
    UserGroup exitGroup(UserGroup ug);
}
