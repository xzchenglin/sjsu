package logi.domain.model.state;

import logi.domain.model.Order;
import logi.domain.model.msg.Sms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Wait4PickState extends OrderState {

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${jms.sms}")
    private String dest;

    @Override
    public void pick(Order order) {

        //TODO generate access code
        jmsTemplate.convertAndSend(dest, new Sms(order.getReceiverPhone(), order.getId(), "111"));

        //TODO trigger pre-payment

        order.setState(State.TobeDeliver);
    }

    @Override
    public void cancel(Order order) {
        order.setState(State.Cancelled);
    }
}
