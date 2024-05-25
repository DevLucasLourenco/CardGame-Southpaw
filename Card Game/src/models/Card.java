package models;

public interface Card {
    public int getAttack();
    public String getName();
    public int getElixirCost();
    public User getUser();
    
    public abstract void setCardDetails();
    public abstract void showCardDetails();

}