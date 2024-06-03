package models;

public interface Card {
    // attributes
    public int getAttack();
    public String getName();
    public int getElixirCost();
    public User getUser();
    public int getAgility();
    
    // methods
    public abstract String exportInfo();
    public abstract void setCardDetails();
    public abstract String getCardDetails(boolean print);
}