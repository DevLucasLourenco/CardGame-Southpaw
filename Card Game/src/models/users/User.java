package models.users;

import java.util.ArrayList;
import java.util.List;
import models.Card;

public class User{
    private String name;
    private int elixir = 10;
    private List<Card> pawUnderControl = new ArrayList<>();

    public String pawInfoText(){
        StringBuilder pawInfo = new StringBuilder();
        for (Card card : this.pawUnderControl) {
            pawInfo.append(card.toString()).append("\n");
        }
        return pawInfo.toString();
    }
    
    
    // Getters & Setters
    public String getName() {
        return name;
    }

    public User(String name){
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getElixir() {
        return elixir;
    }

    public void setElixir(int elixir) {
        this.elixir = elixir;
    }
    
    public void setPawUnderControl(List<Card> pawUnderControl) {
        this.pawUnderControl = pawUnderControl;
    }
    
    public List<Card> getPawUnderControl() {
        return pawUnderControl;
    }
}