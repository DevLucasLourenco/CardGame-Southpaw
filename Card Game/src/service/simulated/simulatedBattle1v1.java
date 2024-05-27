package service.simulated;

import java.util.List;
import models.Card;
import models.PawCard;
import models.User;
import models.characters.*;
import service.event.By;
import service.event.PartyDetection;
import service.event.eventDeal;
import service.event.shiftDeal;

public class simulatedBattle1v1 {
    public static void main(String[] args) {

        User user1 = new User("Lucas");
        User user2 = new User("Fulano");


        PawCard pawarior = new Pawarrior(user1);
        pawarior.positionateCard();
        // pawarior.showCardDetails();

        PawCard pawarior11 = new Pawskeleton(user1);
        pawarior11.positionateCard();
        
        PawCard pawarior2 = new Pawarrior(user2);
        pawarior2.positionateCard();
        // pawarior2.showCardDetails();

        
        
        PartyDetection party = new PartyDetection();
        party.insertUsers(user1, user2);
        
        shiftDeal shiftdeal = new shiftDeal(user1, user2);
        List<Card> sequence = shiftdeal.iterThroughtBy(By.AGILITY);

        System.out.println(sequence);
        
        for (Card card:sequence){
            System.out.println(card.getUser().getName());
        }

        eventDeal event = new eventDeal("SouthPaw");
        event.inicializationFirstEvent();
        event.inputPlayers(user1, user2);
        event.generalState();


        
        

        

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
