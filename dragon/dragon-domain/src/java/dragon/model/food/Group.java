package dragon.model.food;

import java.util.List;
import java.util.Map;

/**
 * Created by lin.cheng
 */
public class Group {

    Long id;
    String name;
    String alias;
    String preference;
    Boolean active;
    Boolean noApprove;

    //transit
    Boolean admin;
    Map<String, Stat> stats;
    List<User> users;
    List<Record> records;

    public Group(String name, String preference) {
        this.name = name;
        this.preference = preference;
    }

    public Group(Long id, String name, String alias, String preference, Boolean active, Boolean noApprove, Boolean admin) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.preference = preference;
        this.active = active;
        this.noApprove = noApprove;
        this.admin = admin;
    }

    public Group(Long id, String name, String alias, String preference, Boolean active, Boolean noApprove) {
        this.id = id;
        this.name = name;
        this.alias = alias;
        this.preference = preference;
        this.active = active;
        this.noApprove = noApprove;
    }

    public Group() {
    }

    public Long getId() {
        return id;
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

    public String getAlias() {
        if(alias == null){
            return name;
        }
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public Boolean getActive() {
        if(active == null){
            return true;
        }
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getNoApprove() {
        if(noApprove == null){
            return true;
        }
        return noApprove;
    }

    public void setNoApprove(Boolean noApprove) {
        this.noApprove = noApprove;
    }

    public Map<String, Stat> getStats() {
        return stats;
    }

    public void setStats(Map<String, Stat> stats) {
        this.stats = stats;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }
}
