package app.domain.model.state;

import app.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class InitState extends OrderState {

    @Override
    public void place(Order order) {
        order.setState(State.Placed);
    }

    @Override
    public void cancel(Order order) {
        order.setState(State.Cancelled);
    }
}
