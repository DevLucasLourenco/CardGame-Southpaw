package models.powerEnviroment;

import models.PawCard;
import models.users.User;

public class tanker extends PowerSkill{

    public tanker(User user, PawCard card, int elixirCost) {
        super(user, card, elixirCost);
    }

    @Override
    void powerRule() {
        int HPactual = getCard().getLife();
        int HPincreased = (int) (HPactual * 1.2);
        getCard().setLife(HPincreased);
    }
}
