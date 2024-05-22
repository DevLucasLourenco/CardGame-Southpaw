package models;

public class Pawarrior extends PawCard {

    public Pawarrior(User user){
        super(user, "Pawarrior");
        setDetailsCharacter();
    }

    private void setDetailsCharacter(){
        setLife(5000);
        setAttack(500);
        setElixirCost(4);
    }

}
