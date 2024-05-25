package models;

import java.util.List;

public class User{
    private String name;
    private int elixir = 10;
    public List<Card> PawUnderControl;
    

    public User(String name){
        this.name = name;
    }

    // Getters & Setters
    public String getName() {
        return name;
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

}