package models.characters;

import models.characters.pawbase.PawCard;
import models.contracts.HasPower;
import models.powerEnviroment.PowerSkill;
import models.powerEnviroment.Doppelgangers;
import models.users.User;

/** Glass cannon: high attack, low HP, can clone itself. */
public class Pawclown extends PawCard implements HasPower {

    public Pawclown(User user) {
        super(user);
    }

    @Override
    public void setCardDetails() {
        setLife(800);
        setAttack(400);
        setAgility(4);
        setRarity(3);
        setElixirCost(3);
    }

    @Override
    public void usePower() {
        PowerSkill power = new Doppelgangers(getUser(), this, getElixirCost());
        power.Use();
    }
}
