package model;

import java.util.List;

public class User {
    public User() {
    }

    long id;
    String mail;
    String name;
    String gender;
    String phone;
    String externalid;

    List<UserSchool> userSchools;
    List<UserGroup> userGroups;

    public String getExternalid() {
        return externalid;
    }

    public void setExternalid(String externalid) {
        this.externalid = externalid;
    }

    public List<UserSchool> getUserSchools() {
        return userSchools;
    }

    public void setUserSchools(List<UserSchool> userSchools) {
        this.userSchools = userSchools;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
