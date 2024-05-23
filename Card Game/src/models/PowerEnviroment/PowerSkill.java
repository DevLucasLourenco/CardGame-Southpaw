package models.PowerEnviroment;

import models.Card;
import models.User;

public abstract class PowerSkill {
    private int elixirCost;
    private Card card;
    private User user;

    public PowerSkill(User user, Card card, int elixirCost){
        this.elixirCost = elixirCost;
        this.card = card;
        this.user = user;
    }

    abstract void powerRule();

    
    public void Use(){
        powerRule();
    }
} 