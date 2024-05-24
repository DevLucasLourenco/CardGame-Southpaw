package models.characters;

import models.PawCard;
import models.PowerEnviroment.*;
import models.User;


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
        PowerSkill power = new rageFromPawCard(getUser(), this, getElixirCost());
        power.Use();
    }
}
