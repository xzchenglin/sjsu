package logi.domain.model.state;

import logi.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class Wait4PickState extends OrderState {

    @Override
    public void pick(Order order) {
        order.setState(State.TobeDeliver);

        //TODO send code to receiver
    }

    @Override
    public void cancel(Order order) {
        order.setState(State.Cancelled);
    }
}
