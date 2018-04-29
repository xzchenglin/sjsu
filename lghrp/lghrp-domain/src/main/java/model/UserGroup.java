package model;

public class UserGroup {
    Group group;
    boolean admin;

    Long gid;
    Long uid;

    public UserGroup() {
    }

    public UserGroup(boolean admin, Long gid, Long uid) {
        this.admin = admin;
        this.gid = gid;
        this.uid = uid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
