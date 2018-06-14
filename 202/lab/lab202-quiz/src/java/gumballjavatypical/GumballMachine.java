package gumballjavatypical;

public class GumballMachine
{

    private int num_gumballs;
    private boolean has_quarter;
    private TYPE type;
    private int current;

    public GumballMachine(int size, TYPE t)
    {
        // initialise instance variables
        this.num_gumballs = size;
        this.has_quarter = false;
        this.type = t;
    }

    public void insert(COIN coin) {
        System.out.println("inserting " + coin);

        if (type.isQuarterOnly() && coin != COIN.QUARTER ) {
            System.out.println( "Quarter only." ) ;
            return;
        }

        if (has_quarter){
            System.out.println("Already inserted enough coins.");
        }

        current += coin.getValue();
        if(current >= type.getCost()){
            has_quarter = true;
        }
    }

    public void turnCrank()
    {
        if ( this.has_quarter )
        {
            if ( this.num_gumballs > 0 )
            {
                this.num_gumballs-- ;
                this.current -= type.getCost();
                if(current < type.getCost()){
                    has_quarter = false;
                }
                System.out.println( "Thanks for your coins.  Gumball Ejected!" ) ;
            }
            else
            {
                System.out.println( "No More Gumballs!  Sorry, can't return your coins." ) ;
            }
        }
        else
        {
            System.out.println( "Please insert enough coins." ) ;
        }
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