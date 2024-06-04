package models.characters;

import models.PawCard;

public class Pawskeleton extends PawCard{

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
        throw new UnsupportedOperationException("Unimplemented method 'usePower'");
    }
}
