package logi.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "bc_addr")
public class Address extends BaseEntity {
    String country;
    String state;
    String city;
    String zip;
    String addr;
    String contact;
    @Column(name = "is_primary")
    boolean isPrimary;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @JsonIgnore
    @org.codehaus.jackson.annotate.JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        this.isPrimary = primary;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", zip='" + zip + '\'' +
                ", addr='" + addr + '\'' +
                ", contact='" + contact + '\'' +
                ", isPrimary=" + isPrimary +
                '}';
    }
}
