package service.battle.simulated;

import java.util.List;
import models.characters.*;
import models.characters.pawbase.PawCard;
import models.contracts.Card;
import models.users.User;
import service.event.By;
import service.event.PartyDetection;
import service.event.eventDeal;
import service.event.shiftDeal;

public class simulatedBattle1v1 {
    public static void main(String[] args) {

        // User 1
        //========================================
        User user1 = new User("Lucas");
        PawCard pawarior = new Pawarrior(user1);
        pawarior.setNickName("Real Knight OG");
        pawarior.positionateCard();
        
        PawCard pawskeleton = new Pawskeleton(user1);
        pawskeleton.positionateCard();
        //========================================
        
        // User 2
        //========================================
        User user2 = new User("Fulano");
        PawCard pawarior2 = new Pawarrior(user2);
        pawarior2.positionateCard();
        
        PawCard pawskeleton2 = new Pawskeleton(user2);
        pawskeleton2.positionateCard();
        //========================================

        // Party and iteration by agility
        //========================================
        PartyDetection party = new PartyDetection();
        party.insertUsers(user1, user2);
        
        shiftDeal shiftdeal = new shiftDeal(user1, user2);
        List<Card> sequence = shiftdeal.iterThroughtBy(By.AGILITY);
        //========================================
        
        // Event Deal
        //========================================
        eventDeal event = new eventDeal();
        
        event.insertUsers(user1, user2);
        event.generalBattleState();
        
        List<Card> sortedcard = shiftdeal.actionOrderingByAgility(sequence);
        System.out.println(sortedcard);
        //========================================

    }
}
