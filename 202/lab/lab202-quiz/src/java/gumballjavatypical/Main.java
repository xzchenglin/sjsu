package gumballjavatypical;

public class Main {
    public static void main(String[] args) {
        testTypeA();
        testTypeB();
        testTypeC();
    }

    private static void testTypeA(){
        System.out.println("---------Testing A---------");

        GumballMachine gumballMachine = new GumballMachine(5, GumballMachine.TYPE.A);

        System.out.println(gumballMachine);

        gumballMachine.insert(GumballMachine.COIN.DIME );

        gumballMachine.insert(GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);

        gumballMachine.insert( GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();
        gumballMachine.insert( GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);
    }

    private static void testTypeB(){
        System.out.println("---------Testing B---------");

        GumballMachine gumballMachine = new GumballMachine(5, GumballMachine.TYPE.B);

        System.out.println(gumballMachine);

        gumballMachine.insert(GumballMachine.COIN.NICKLE );

        gumballMachine.insert(GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);

        gumballMachine.insert( GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();
        gumballMachine.insert( GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);
    }

    private static void testTypeC(){
        System.out.println("---------Testing C---------");

        GumballMachine gumballMachine = new GumballMachine(5, GumballMachine.TYPE.C);

        System.out.println(gumballMachine);

        gumballMachine.insert(GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);

        gumballMachine.insert( GumballMachine.COIN.DIME );
        gumballMachine.insert( GumballMachine.COIN.NICKLE );
        gumballMachine.insert( GumballMachine.COIN.NICKLE );
        gumballMachine.insert( GumballMachine.COIN.NICKLE );
        gumballMachine.insert( GumballMachine.COIN.NICKLE );
        gumballMachine.insert( GumballMachine.COIN.NICKLE );
        gumballMachine.insert( GumballMachine.COIN.DIME );
        gumballMachine.turnCrank();
        gumballMachine.insert( GumballMachine.COIN.QUARTER );
        gumballMachine.turnCrank();

        System.out.println(gumballMachine);
    }

}
