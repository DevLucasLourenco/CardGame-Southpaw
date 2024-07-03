package models.characters;

import models.characters.pawbase.PawCard;
import models.contracts.hasPower;

public class Pawbayle extends PawCard implements hasPower{

    public Pawbayle(models.users.User user){
        super(user);
    }

    @Override
    public void usePower() {
        throw new UnsupportedOperationException("Unimplemented method 'usePower'");
    }

    @Override
    public void setCardDetails() {
        throw new UnsupportedOperationException("Unimplemented method 'setCardDetails'");
    }

}
