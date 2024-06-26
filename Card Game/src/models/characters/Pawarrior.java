package models.characters;

import models.characters.pawbase.PawCard;
import models.contracts.hasPower;
import models.powerEnviroment.*;
import models.users.User;


public class Pawarrior extends PawCard implements hasPower{

    public Pawarrior(User user){
        super(user);
    }
    
    @Override
    public void setCardDetails(){
        setLife(3000);
        setAttack(300);
        setAgility(2);
        setRarity(4);
        setElixirCost(4);
    }

    @Override
    public void usePower() {
        PowerSkill power = new rage(getUser(), this, getElixirCost());
        power.Use();
    }
}
