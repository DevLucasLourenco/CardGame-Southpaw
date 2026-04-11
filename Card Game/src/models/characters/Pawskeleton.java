package models.characters;

import models.characters.pawbase.PawCard;
import models.contracts.HasPower;

public class Pawskeleton extends PawCard implements HasPower{

    public Pawskeleton(models.users.User user) {
        super(user);
    }

    @Override
    public void setCardDetails() {
        setLife(600);
        setAttack(100);
        setAgility(5);
        setRarity(2);
        setElixirCost(2);
    }

    @Override
    public void usePower() {
        // TODO: implement Pawskeleton's unique power
    }
}
