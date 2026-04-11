package models.characters;

import models.characters.pawbase.PawCard;
import models.contracts.HasPower;
import models.powerEnviroment.*;
import models.users.User;


public class Pawarrior extends PawCard implements HasPower{

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
        PowerSkill power = new Rage(getUser(), this, getElixirCost());
        power.Use();
    }
}
