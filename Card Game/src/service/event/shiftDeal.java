package service.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
        Collections.shuffle(resultList);
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

    public List<Card> actionOrdering(List<Card> sortedCards) {
        List<Card> actionOrder = new ArrayList<>();
        int minNumber = getMinNumber(sortedCards);

        for (Card card : sortedCards){
            // CC -> Current Card
            int agilityPointCC = card.getAgility();
            boolean enought = false;

            while (true){
                if (agilityPointCC>minNumber){
                    enought = true;
                    actionOrder.add(card);
                    agilityPointCC -= minNumber;

                } else if (agilityPointCC==minNumber) {
                    actionOrder.add(card);
                    break;
                    
                } else if (agilityPointCC<minNumber){
                    if (enought){
                        actionOrder.add(card);
                    }
                    break;
                }
            }
        }
        // return shuffleActionOrder(actionOrder);
        return actionOrder;
    }

    private List<Card> shuffleActionOrder(List<Card> actionOrder){
        // lista de 1 de velocidade -> 
        // lista de 2 de velocidade -> 
        // lista de 3 de velocidade -> 
        // lista de 4 de velocidade -> 
        // lista de 5 de velocidade -> 

        // extend em uma lista após shuffled na ordem do mais rapido ao mais lento (5 > 1)
        
    }

    private Integer getMinNumber(List<Card> listSorted){
        int minNumber = 0;
        int auxInt;
        
        for (Card card : listSorted){
            if (minNumber==0){
                auxInt = card.getAgility();
                minNumber = auxInt;
            } else if (card.getAgility() < minNumber){
                minNumber = card.getAgility();
            }
        }
        return minNumber;
    }

    // Getter
    public List<User> getUserList() {
        return userList;
    }
    
}
