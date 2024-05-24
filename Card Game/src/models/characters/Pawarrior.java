package models.characters;

import models.PawCard;
import models.User;
import models.powerEnviroment.*;


public class Pawarrior extends PawCard {

    public Pawarrior(User user){
        super(user, "Pawarrior");
    }
    
    {setCardDetails();}

    @Override
    public void setCardDetails(){
        setLife(5000);
        setAttack(300);
        setElixirCost(4);
    }

    @Override
    public void usePower() {
        PowerSkill power = new rage(getUser(), this, getElixirCost());
        power.Use();
    }
}
