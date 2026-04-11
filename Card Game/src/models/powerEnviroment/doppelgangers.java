package models.powerEnviroment;

import models.characters.pawbase.PawCard;
import models.users.User;

public class Doppelgangers extends PowerSkill{

    public Doppelgangers(User user, PawCard card, int elixirCost) {
        super(user, card, elixirCost);
    }

    @Override
    void powerRule() {
        try {
            PawCard clone = getCard().getClass()
                    .getDeclaredConstructor(models.users.User.class)
                    .newInstance(getUser());
            clone.forcePositionateCard();
            System.out.printf("A doppelganger of %s has appeared on the field!%n", getCard().getName());
        } catch (Exception e) {
            System.out.println("Doppelganger failed: " + e.getMessage());
        }
    }
}
