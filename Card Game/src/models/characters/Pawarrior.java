package models.characters;

import models.PawCard;
import models.User;
import models.powerEnviroment.*;


public class Pawarrior extends PawCard {

    public Pawarrior(User user){
        super(user);
    }
    
    @Override
    public void setCardDetails(){
        setLife(5000);
        setAttack(300);
        setAgility(2);
        setspeed(1);
        setRarity(4);
        setElixirCost(4);
    }

    @Override
    public void usePower() {
        PowerSkill power = new rage(getUser(), this, getElixirCost());
        power.Use();
    }
}
