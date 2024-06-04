package models.powerEnviroment;

import models.characters.pawbase.PawCard;
import models.users.User;

public abstract class PowerSkill {
    private final int elixirCost;
    private final PawCard card;
    private final User user;

    public PowerSkill(User user, PawCard card, int elixirCost){
        this.elixirCost = elixirCost;
        this.card = card;
        this.user = user;
    }

    abstract void powerRule();

    
    public void Use(){
        powerRule();
    }

    
    // Getters & Setters
    public int getElixirCost() {
        return elixirCost;
    }


    public PawCard getCard() {
        return card;
    }


    public User getUser() {
        return user;
    }

} 