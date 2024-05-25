package service.simulated;

import models.PawCard;
import models.User;
import models.characters.*;
import service.event.Party;

public class simulatedBattle1v1 {
    public static void main(String[] args) {

        User user1 = new User("Lucas");
        User user2 = new User("Fulano");


        PawCard pawarior = new Pawarrior(user1);
        pawarior.positionateCard();
        // pawarior.showCardDetails();

        PawCard pawarior11 = new Pawarrior(user1);
        pawarior11.positionateCard();
        
        PawCard pawarior2 = new Pawarrior(user2);
        pawarior2.positionateCard();
        // pawarior2.showCardDetails();

        
        
        Party party = new Party();
        party.insertUsers(user1, user2);
        

        // pawarior2.showCardDetails();
        
        // pawarior.usePower();
        // pawarior.showCardDetails();
        
        // for (int i = 0; i < 9; i++) {
        //     pawarior.attackEnemy(pawarior2);
        // }
        // pawarior2.showCardDetails();

        // System.out.println(user2.getPawUnderControl());  
    }
}
