package models.users;

import java.util.ArrayList;
import java.util.List;
import models.contracts.Card;

public class User{
    private String name;
    private int elixir = 10;
    private int playerHP    = 8000;
    private int maxPlayerHP = 8000;
    private List<Card> pawUnderControl = new ArrayList<>();
    private Statistic statistic = new Statistic(this);



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

    public Statistic getStatistic() {
        return statistic;
    }

    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    public int getPlayerHP()    { return playerHP; }
    public int getMaxPlayerHP() { return maxPlayerHP; }

    public void takeDamage(int dmg) {
        playerHP = Math.max(0, playerHP - dmg);
    }

    public boolean isAlive() {
        return playerHP > 0;
    }

    public boolean hasRoom() {
        return pawUnderControl.size() < 5;
    }
}
