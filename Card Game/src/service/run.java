package service;

import models.PawCard;
import models.Pawarrior;
import models.User;

public class run {
    public static void main(String[] args) {
        User user1 = new User("Lucas");
        PawCard pawwarrior = new Pawarrior(user1);
        pawwarrior.positionateCard();
        pawwarrior.showCardDetails();
        
        User user2 = new User("Fulano");
        PawCard pawwarrior2 = new Pawarrior(user2);
        pawwarrior2.positionateCard();
        pawwarrior2.showCardDetails();
        
        pawwarrior.attackEnemy(pawwarrior2);
        System.out.println(pawwarrior2.getLife());
    }
}
