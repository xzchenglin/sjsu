package logi.domain.model.state;

import logi.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class TobeDeliverState extends OrderState {

    @Override
    public void pick(Order order) {
        //allowed for multiple driver
    }

    @Override
    public void deliver(Order order) {
        order.setState(State.Success);

        //TODO trigger payment
    }

    @Override
    public void fail(Order order) {
        order.setState(State.Failed);
    }
}
