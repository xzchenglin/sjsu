package logi.domain.model.msg;

import java.io.Serializable;

public class Sms implements Serializable {
    public Sms() {
    }

    public Sms(String number, Long orderId, String accessCode) {
        this.number = number;
        this.orderId = orderId;
        this.accessCode = accessCode;
    }

    String number;
    Long orderId;
    String accessCode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    @Override
    public String toString() {
        return "Sms{" +
                ", number='" + number + '\'' +
                ", orderId=" + orderId +
                ", accessCode='" + accessCode + '\'' +
                '}';
    }
}
