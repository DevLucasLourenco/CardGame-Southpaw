package service.simulated;

import models.PawCard;
import models.User;
import models.characters.*;

public class simulatedBattle1v1 {
    public static void main(String[] args) {

        User user1 = new User("Lucas");
        User user2 = new User("Fulano");


        PawCard pawarior = new Pawarrior(user1);
        pawarior.positionateCard();
        pawarior.showCardDetails();

        PawCard pawarior2 = new Pawarrior(user2);
        pawarior2.positionateCard();
        pawarior2.showCardDetails();
        
        pawarior.usePower();
        pawarior.showCardDetails();
            
        for (int i = 0; i < 9; i++) {
            pawarior.attackEnemy(pawarior2);
        }
        pawarior2.showCardDetails();
    }
}
