package models;

import java.util.ArrayList;
import java.util.List;

public class User{
    private String name;
    private int elixir = 10;
    private List<Card> pawUnderControl = new ArrayList<>();

    public String pawInfoText(){
        List<String> txtlist = new ArrayList<>();
        String txtTreat = "";

        for (Card paw : this.pawUnderControl){
            txtlist.add(paw.showCardDetails(false));
        }

        if (!(txtlist.isEmpty())){
            txtTreat += "==========\n";
            for (String txt : txtlist){
                txtTreat += "|----------\n";
                txtTreat += txt+"\n";
                txtTreat += "|----------\n";
            }
            txtTreat += "==========\n";
        }
        return txtTreat;
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