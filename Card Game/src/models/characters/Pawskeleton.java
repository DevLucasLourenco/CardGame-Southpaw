package models.characters;

import models.PawCard;

public class Pawskeleton extends PawCard{

    public Pawskeleton(models.User user, String name) {
        super(user, name);
    }

    {setCardDetails();}

    @Override
    public void setCardDetails() {
        setLife(350);
        setAttack(50);
        setElixirCost(1);
    }

    @Override
    public void usePower() {
        throw new UnsupportedOperationException("Unimplemented method 'usePower'");
    }
    
}
