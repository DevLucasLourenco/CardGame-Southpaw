package service;

import models.PawCard;
import models.User;
import models.characters.*;

public class battle1v1 {
    public static void main(String[] args) {
        User user1 = new User("Lucas");
        User user2 = new User("Fulano");


        PawCard pawwarrior = new Pawarrior(user1);
        pawwarrior.positionateCard();
        pawwarrior.showCardDetails();

        PawCard pawwarrior2 = new Pawarrior(user2);
        pawwarrior2.positionateCard();
        pawwarrior2.showCardDetails();
        
        pawwarrior.usePower();
        pawwarrior.showCardDetails();
            
        
        pawwarrior.attackEnemy(pawwarrior2);
        System.out.println(pawwarrior2.getLife());
    }
}
