package gumballjavapattern;

import java.util.Random;

public class HasQuarterState implements State {
	GumballMachine gumballMachine;
 
	public HasQuarterState(GumballMachine gumballMachine) {
		this.gumballMachine = gumballMachine;
	}
  
	public void insert(GumballMachine.COIN coin) {
		System.out.println("You can't insert another coin.");
	}
 
	public void ejectQuarter() {
		gumballMachine.setCurrent(0);
        gumballMachine.setState(gumballMachine.getNoQuarterState());
        System.out.println(gumballMachine.getCurrent() + " returned");
	}
 
	public void turnCrank() {
		System.out.println("You turned...");
		gumballMachine.setState(gumballMachine.getSoldState());
	}

    public void dispense() {
        System.out.println("No gumball dispensed");
    }
 
	public String toString() {
		return "waiting for turn of crank";
	}
}
