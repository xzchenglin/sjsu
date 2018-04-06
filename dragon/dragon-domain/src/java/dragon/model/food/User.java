 package dragon.model.food;

/**
 * Created by lin.cheng
 */
public class User {
    Long id;
    String name;
    String alias;
    String email;
    String pwd;
    NotifyType notifyType;

    public User(String email) {
        this.email = email;
        this.name = email;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public NotifyType getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(NotifyType notifyType) {
        this.notifyType = notifyType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public static enum NotifyType {
        Push,
        Email,
        None
    }
}
