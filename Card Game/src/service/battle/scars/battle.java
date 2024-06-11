package service.battle.scars;

import java.util.Scanner;
import models.users.User;
import service.event.PartyDetection;
import service.event.eventDeal;

public class battle {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Receive Names
        //========================================
        System.out.println("Set your infos.\n---------------------------------\n");
        System.out.println("About the FIRST player, what's your name?");
        String firstName = scanner.nextLine().toUpperCase();
        
        System.out.println("About the SECOND player, what's your name?");
        String secondName = scanner.nextLine().toUpperCase();
        //========================================

        // Settings Players
        //========================================
        User user1 = new User(firstName);
        User user2 = new User(secondName);
        //========================================
        
        // Primordial Settings and Instanciations
        //========================================
        PartyDetection party = new PartyDetection();
        eventDeal event = new eventDeal();
        
        party.insertUsers(user1, user2);
        event.insertUsers(user1, user2);
        //========================================

        // Invoke
        //========================================
        event.FirstMenu();


        //dps
        event.ChooseMenu();
        //========================================

    }
}