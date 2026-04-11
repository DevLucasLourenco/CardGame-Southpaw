package models.characters;

import models.characters.pawbase.PawCard;
import models.contracts.HasPower;
import models.powerEnviroment.PowerSkill;
import models.powerEnviroment.Tanker;
import models.users.User;

/** Balanced fighter: can spend Elixir to boost its own HP. */
public class Pawbayle extends PawCard implements HasPower {

    public Pawbayle(User user) {
        super(user);
    }

    @Override
    public void setCardDetails() {
        setLife(1800);
        setAttack(200);
        setAgility(3);
        setRarity(3);
        setElixirCost(3);
    }

    @Override
    public void usePower() {
        PowerSkill power = new Tanker(getUser(), this, getElixirCost());
        power.Use();
    }
}
