package models.characters;

import models.characters.pawbase.PawCard;
import models.contracts.hasPower;

public class Pawclown extends PawCard implements hasPower{

    public Pawclown(models.users.User user) {
        super(user);
    }

    @Override
    public void setCardDetails() {
    }

    @Override
    public void usePower() {
    }
    
}