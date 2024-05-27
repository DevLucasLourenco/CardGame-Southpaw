package service.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import models.User;

public class eventDeal {
    protected String GameName;
    protected List<User> users = new ArrayList<>();;

    Scanner scanner = new Scanner(System.in);

    public eventDeal(String GameName){
        this.GameName = GameName;
    }


    public void inicializationFirstEvent(){
        System.out.println(String.format("===== %s =====", this.GameName));
        System.out.println(String.format("Good Luck!", this.GameName));
    }

    public void inputPlayers(User... users){
        this.users.addAll(Arrays.asList(users));
    }

    public void generalState(){
        if (!(this.users.isEmpty())){
            for (User user : this.users){
                String texto =String.format("|-----%s-----\n", user.getName())+
                String.format("|%s - Elixir: %s\n", user.getName(), user.getElixir())+
                String.format("|%s - Paws: \n%s\n", user.getName(), user.pawInfoText());
                
    
                System.out.println(String.format(texto));
                // retornar aqui todo o estado da batalha, quanto de elixir os usuarios tem, hp de todos os monstros e tudo mais
            }
        }
    }

    public void apresentation(){
        // apresentar os usuarios
    }
}
