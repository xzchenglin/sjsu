package gumballjavapattern;

public class NoQuarterState implements State {
    GumballMachine gumballMachine;
 
    public NoQuarterState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }
 
	public void insert(GumballMachine.COIN coin) {
		System.out.println("Inserting " + coin);
		if(coin != GumballMachine.COIN.QUARTER && gumballMachine.getType().isQuarterOnly()){
            System.out.println( "Quarter only." ) ;
            return;
        }
        gumballMachine.setCurrent(gumballMachine.getCurrent() + coin.getValue());
        if(gumballMachine.getCurrent() >= gumballMachine.getType().getCost()) {
            gumballMachine.setState(gumballMachine.getHasQuarterState());
        }
	}
 
	public void ejectQuarter() {
        if(gumballMachine.getCurrent() == 0) {
            System.out.println("You haven't inserted coins");
            return;
        }
        gumballMachine.setCurrent(0);
        System.out.println(gumballMachine.getCurrent() + " returned");
    }
 
	public void turnCrank() {
		System.out.println("You turned, but there's not enough coins.");
	 }
 
	public void dispense() {
		System.out.println("You need to pay first");
	} 
 
	public String toString() {
		return "waiting for coins";
	}
}
