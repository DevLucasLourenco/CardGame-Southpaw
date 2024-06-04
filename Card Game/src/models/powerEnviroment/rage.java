package models.powerEnviroment;

import models.PawCard;
import models.users.User;

public class rage extends PowerSkill{

    public rage(User user, PawCard card, int elixirCost) {
        super(user, card, elixirCost);
    }

    @Override
    void powerRule() {
        getCard().setAttack(getCard().getAttack()*2);
        
    }
}