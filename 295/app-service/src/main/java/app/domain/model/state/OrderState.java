package app.domain.model.state;

import app.domain.model.Order;

public abstract class OrderState {

    public void place(Order order){
        throw new IllegalStateException(toString());
    }

    public void take(Order order){
        throw new IllegalStateException(toString());
    }

    public void pick(Order order){
        throw new IllegalStateException(toString());
    }

    public void deliver(Order order){
        throw new IllegalStateException(toString());
    }

    public void cancel(Order order){
        throw new IllegalStateException(toString());
    }

    public void fail(Order order){
        throw new IllegalStateException(toString());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
