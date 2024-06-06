package service.battle.scars;

import java.util.Scanner;
import models.users.User;

public class battle {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        //--------------------------------------------------
        // Receive Names
        System.out.println("Set your infos.\n---------------------------------\n");
        System.out.println("To the first player, what's your name?");
        String firstName = scanner.nextLine().toUpperCase();
        
        System.out.println("To the second player, what's your name?");
        String secondName = scanner.nextLine().toUpperCase();

        // Settings Players
        User user1 = new User(firstName);
        User user2 = new User(secondName);

        //--------------------------------------------------



    }
}