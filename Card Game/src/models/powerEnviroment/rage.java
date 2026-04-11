package models.powerEnviroment;

import models.characters.pawbase.PawCard;
import models.users.User;

public class Rage extends PowerSkill{

    public Rage(User user, PawCard card, int elixirCost) {
        super(user, card, elixirCost);
    }

    @Override
    void powerRule() {
        // Always based on baseAttack to prevent stacking from multiple activations
        getCard().setAttack(getCard().getBaseAttack() * 2);
        System.out.printf("%s enters a RAGE! Attack: %d -> %d%n",
                getCard().getName(), getCard().getBaseAttack(), getCard().getAttack());
    }
}
