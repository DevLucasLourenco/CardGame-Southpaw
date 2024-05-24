package models.PowerEnviroment;

import models.PawCard;
import models.User;

public class rageFromPawCard extends PowerSkill{

    public rageFromPawCard(User user, PawCard card, int elixirCost) {
        super(user, card, elixirCost);
    }

    @Override
    void powerRule() {
        getCard().setAttack(getCard().getAttack()*2);
        
    }
}