package logi.domain.model.state;

import logi.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class PlacedState extends OrderState {

    @Override
    public void take(Order order) {
        order.setState(State.Wait4Pick);
    }

    @Override
    public void cancel(Order order) {
        order.setState(State.Cancelled);
    }
}
