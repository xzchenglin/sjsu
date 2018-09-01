package logi.domain.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bc_user")
public class User extends VersionedEntity {

    String name;
    @Enumerated(EnumType.STRING)
    Role role = Role.User;
    String pwd;
    String phone;
    String email;
    @Column(length = 65536)
    String pubkey;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Address> addresses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getPubkey() {
        return pubkey;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", role=" + role +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void setPubkey(String pubkey) {
        this.pubkey = pubkey;
    }

    public enum Role{
        User,
        Driver
    }
}
