package service.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.Card;
import models.User;

public class shiftDeal {
    private final List<User> userList = new ArrayList<>();
    
    
    public shiftDeal(User... users) {
        this.userList.addAll(Arrays.asList(users));
    }

    public List<Card> iterThroughtBy(By option){
        List<Card> result = cardManagement(option);
        return result;
    }

    private List<Card> cardManagement(By option) {
        List<Card> resultList = new ArrayList<>();
        
        for (User currentUser: this.userList){
            for (Card currentCard : currentUser.getPawUnderControl()){
                resultList.add(currentCard);     
            }
        }

        Collections.sort(resultList, new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                switch (option) {
                    case AGILITY:
                        return Integer.compare(c2.getAgility(), c1.getAgility());
                
                    case ATTACK:
                        return Integer.compare(c2.getAttack(), c1.getAttack());

                    default:
                        throw new IllegalArgumentException("Unknow ordenation option: " + option);
                }
            }
        });
        return resultList;
    }

    // Getter
    public List<User> getUserList() {
        return userList;
    }
    
}
