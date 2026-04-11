package models.powerEnviroment;

import models.characters.pawbase.PawCard;
import models.users.User;

public class Tanker extends PowerSkill{

    public Tanker(User user, PawCard card, int elixirCost) {
        super(user, card, elixirCost);
    }

    @Override
    void powerRule() {
        int HPactual = getCard().getLife();
        int HPincreased = (int) (HPactual * 1.2);
        getCard().setMaxLife(HPincreased);
        getCard().setLife(HPincreased);
    }
}
