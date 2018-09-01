package logi.domain.model.state;

public enum State {
    Cancelled(CancelledState.class),
    Failed(FailedState.class),
    Init(InitState.class),
    Placed(PlacedState.class),
    Success(SuccessState.class),
    TobeDeliver(TobeDeliverState.class),
    Wait4Pick(Wait4PickState.class);

    Class<? extends OrderState> state;

    public Class<? extends OrderState> getState() {
        return state;
    }

    State(Class<? extends OrderState> state) {
        this.state = state;
    }
}
