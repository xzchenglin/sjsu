package gumballjavapattern;

public class GumballMachine {
 
	State soldOutState;
	State noQuarterState;
	State hasQuarterState;
	State soldState;
 
	State state = soldOutState;
	int count = 0;

	private TYPE type;
	private int current;

    public GumballMachine(int numberGumballs, TYPE t) {
		soldOutState = new SoldOutState(this);
		noQuarterState = new NoQuarterState(this);
		hasQuarterState = new HasQuarterState(this);
		soldState = new SoldState(this);

		this.count = numberGumballs;
		this.type = t;
 		if (numberGumballs > 0) {
			state = noQuarterState;
		} 
	}
 
	public void insert(GumballMachine.COIN coin) {
		state.insert(coin);
	}
 
	public void ejectQuarter() {
		state.ejectQuarter();
	}
 
	public void turnCrank() {
		state.turnCrank();
		state.dispense();
	}

	void setState(State state) {
		this.state = state;
	}

	void releaseBall() {
		System.out.println("A gumball comes rolling out the slot...");
		if (count != 0) {
			count = count - 1;
		}
	}
 
	int getCount() {
		return count;
	}
 
	void refill(int count) {
		this.count = count;
		state = noQuarterState;
	}

    public State getState() {
        return state;
    }

    public State getSoldOutState() {
        return soldOutState;
    }

    public State getNoQuarterState() {
        return noQuarterState;
    }

    public State getHasQuarterState() {
        return hasQuarterState;
    }

    public State getSoldState() {
        return soldState;
    }
 
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("\nMighty Gumball, Inc.");
		result.append("\nJava-enabled Standing Gumball Model #2004");
		result.append("\nInventory: " + count + " gumball");
		if (count != 1) {
			result.append("s");
		}
		result.append("\n");
		result.append("Machine is " + state + "\n");
		return result.toString();
	}

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    enum COIN{

        NICKLE(1),
        DIME(10),
        QUARTER(25);

        COIN(int value) {
            this.value = value;
        }

        int value;

        public int getValue() {
            return value;
        }
    }

    enum TYPE{
        A(25, true),
        B(50, true),
        C(50, false);

        TYPE(int cost, boolean quarterOnly) {
            this.cost = cost;
            this.quarterOnly = quarterOnly;
        }

        int cost;
        boolean quarterOnly;

        public int getCost() {
            return cost;
        }

        public boolean isQuarterOnly() {
            return quarterOnly;
        }
    }
}
