package models.characters;

import models.PawCard;

public class Pawskeleton extends PawCard{

    public Pawskeleton(models.User user) {
        super(user);
    }

    @Override
    public void setCardDetails() {
        setLife(350);
        setAttack(50);
        setAgility(5);
        setRarity(2);
        setElixirCost(1);
    }

    @Override
    public void usePower() {
        throw new UnsupportedOperationException("Unimplemented method 'usePower'");
    }
}
