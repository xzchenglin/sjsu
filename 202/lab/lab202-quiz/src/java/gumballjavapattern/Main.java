package gumballjavapattern;

public class Main {

	public static void main(String[] args) {
        testTypeA();
        testTypeB();
        testTypeC();
	}

    private static void testTypeA(){
        System.out.println("---------Testing A---------");

        gumballjavapattern.GumballMachine gumballMachine = new gumballjavapattern.GumballMachine(5, gumballjavapattern.GumballMachine.TYPE.A);

        System.out.println(gumballMachine);

        gumballMachine.insert(gumballjavapattern.GumballMachine.COIN.DIME );

        gumballMachine.insert(gumballjavapattern.GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);

        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();
        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);
    }

    private static void testTypeB(){
        System.out.println("---------Testing B---------");

        gumballjavapattern.GumballMachine gumballMachine = new gumballjavapattern.GumballMachine(5, gumballjavapattern.GumballMachine.TYPE.B);

        System.out.println(gumballMachine);

        gumballMachine.insert(gumballjavapattern.GumballMachine.COIN.NICKLE );

        gumballMachine.insert(gumballjavapattern.GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);

        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();
        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);
    }

    private static void testTypeC(){
        System.out.println("---------Testing C---------");

        gumballjavapattern.GumballMachine gumballMachine = new gumballjavapattern.GumballMachine(5, gumballjavapattern.GumballMachine.TYPE.C);

        System.out.println(gumballMachine);

        gumballMachine.insert(gumballjavapattern.GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);

        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.DIME );
        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.NICKLE );
        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.NICKLE );
        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.NICKLE );
        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.NICKLE );
        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.NICKLE );
        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.DIME );
        gumballMachine.turnCrank();
        gumballMachine.insert( gumballjavapattern.GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);
    }

}
