package app.domain.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "bc_user")
public class User extends VersionedEntity {

    @Transient
    static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    String name;
    @Enumerated(EnumType.STRING)
    Role role = Role.User;

    @Transient
    String pwd;
    @Column(name = "pwd")
    String pwdEnc;

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

    public String getPwdEnc() {
        return pwdEnc;
    }

    public void setPwdEnc(String pwdEnc) {
        this.pwdEnc = pwdEnc;
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

    public User unload(){
        addresses = null;
        return this;
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

    @PreUpdate
    @PrePersist
    public void beforeUpdate() {
        if(StringUtils.isNotBlank(pwd)) {
            pwdEnc = bCryptPasswordEncoder.encode(pwd);
        }
    }
}
