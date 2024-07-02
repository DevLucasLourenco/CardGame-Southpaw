package models.users;

import java.util.ArrayList;
import java.util.List;
import models.contracts.Card;

public class User{
    private String name;
    private int elixir = 10;
    private List<Card> pawUnderControl = new ArrayList<>();
    private Statistic statistic = new Statistic(this);
    // Quando o monstro inimgo ataca diretamente o player, é descontado 
    //exatamente o valor do elixir do monstro do elixir do jogador. 
    //Quando chegar a 0, acaba o player perder 
    //(acontece também se o próprio usuário fazer o elixir chegar a 0)


    
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
}