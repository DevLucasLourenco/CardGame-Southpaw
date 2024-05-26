package service.event;

import java.util.Scanner;

public class eventDeal {
    protected String GameName;

    Scanner scanner = new Scanner(System.in);

    public eventDeal(String GameName){
        this.GameName = GameName;
    }


    public void inicializationFirstEvent(){
        System.out.println(String.format("===== %s =====", this.GameName));
        System.out.println(String.format("Good Luck!", this.GameName));
    }

    public void generalState(){
        // retornar aqui todo o estado da batalha, quanto de elixir os usuarios tem, hp de todos os monstros e tudo mais
    }

    public void apresentation(){
        // apresentar os usuarios
    }
}
