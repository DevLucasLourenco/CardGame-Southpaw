package models;

public interface Card {
    public int getAttack();
    public String getName();
    public int getElixirCost();
    public User getUser();
    public int getAgility();
    
    public abstract void setCardDetails();
    public abstract String showCardDetails(boolean print);

}