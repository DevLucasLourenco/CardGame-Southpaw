package models.powerEnviroment;

import models.PawCard;
import models.User;

public class doppelgangers extends PowerSkill{

    public doppelgangers(User user, PawCard card, int elixirCost) {
        super(user, card, elixirCost);
    }

    @Override
    void powerRule() {
        // duplicar;
    }
}