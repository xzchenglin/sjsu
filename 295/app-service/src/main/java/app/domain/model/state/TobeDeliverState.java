package app.domain.model.state;

import app.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class TobeDeliverState extends OrderState {

    @Override
    public void pick(Order order) {
        //allowed for multiple driver
    }

    @Override
    public void deliver(Order order) {
        //TODO verify access code
        //TODO finish payment

        order.setState(State.Success);
    }

    @Override
    public void fail(Order order) {
        order.setState(State.Failed);
    }
}
