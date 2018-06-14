package gumballjavapattern;

public interface State {
 
	public void insert(GumballMachine.COIN coin);
	public void ejectQuarter();
	public void turnCrank();
	public void dispense();
}
