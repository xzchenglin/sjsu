package logi.service.jms;

import logi.domain.model.msg.Sms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SmsHandler {

    @JmsListener(destination = "${jms.sms}", containerFactory = "defaultFactory")
    public void receiveMessage(Sms sms) {
        System.out.println("Received <" + sms + ">");

        //TODO send to receiver
    }

}
