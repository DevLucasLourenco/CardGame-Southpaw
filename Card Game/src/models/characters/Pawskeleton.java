package models.characters;

import models.characters.pawbase.PawCard;
import models.contracts.hasPower;

public class Pawskeleton extends PawCard implements hasPower{

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
