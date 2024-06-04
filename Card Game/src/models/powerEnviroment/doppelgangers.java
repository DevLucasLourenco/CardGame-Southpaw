package models.powerEnviroment;

import models.characters.pawbase.PawCard;
import models.users.User;

public class doppelgangers extends PowerSkill{

    public doppelgangers(User user, PawCard card, int elixirCost) {
        super(user, card, elixirCost);
    }

    @Override
    void powerRule() {
        // duplicar;
    }
}