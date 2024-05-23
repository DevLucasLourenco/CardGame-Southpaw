package models;


public class Pawarrior extends PawCard {

    public Pawarrior(User user){
        super(user, "Pawarrior");
    }
    
    {
        setCardDetails();
    }

    @Override
    public void setCardDetails(){
        setLife(5000);
        setAttack(500);
        setElixirCost(4);
    }

    @Override
    public void showCardDetails() {
        System.out.println(getLife());
        System.out.println(getAttack());
        System.out.println(getElixirCost());
        System.out.println(getUser().getName());
        System.out.println("\n");
    }

    @Override
    public void usePower() {
        
    }

    
}
