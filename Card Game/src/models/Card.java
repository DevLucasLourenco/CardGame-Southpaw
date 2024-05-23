package models;

public interface Card {
    public int getAttack();
    public String getName();
    public int getElixirCost();

    public abstract void setCardDetails();
    public abstract void showCardDetails();

}