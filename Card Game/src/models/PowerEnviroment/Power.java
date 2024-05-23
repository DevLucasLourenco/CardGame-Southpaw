package models.PowerEnviroment;

import models.Card;
import models.User;

public abstract class Power {
    private int elixirCost;
    private Card card;
    private User user;

    public Power(User user, Card card, int elixirCost){
        this.elixirCost = elixirCost;
        this.card = card;
        this.user = user;
    }

    abstract void powerRule();

    
    public void Use(){
        powerRule();
    }
} 