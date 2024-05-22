package models;


public class Pawarrior extends PawCard {

    public Pawarrior(User user){
        super(user, "Pawarrior");
        detailsCharacter();
    }

    private void detailsCharacter(){
        setLife(5000);
        setAttack(500);
        setElixirCost(4);
    }

}
