package service.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.contracts.Card;
import models.users.User;

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

    public List<Card> actionOrderingByAgility(List<Card> sortedCards) {
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
        return shuffleActionOrder(actionOrder);
    }

    private List<Card> shuffleActionOrder(List<Card> actionOrder){
        List<Card> shuffledActionOrder = new ArrayList<>();
        Map<Integer, List<Card>> agilityGroups = new HashMap<>();

        for (Card card : actionOrder){
            // CC -> Current Card
            int agilityCC = card.getAgility();
            agilityGroups.computeIfAbsent(agilityCC, k -> new ArrayList<>()).add(card);
        }

        List<Integer> agilityLevels = new ArrayList<>(agilityGroups.keySet());
        agilityLevels.sort(Collections.reverseOrder());

        for (int agility : agilityLevels){
            List<Card> cards = agilityGroups.get(agility);
            Collections.shuffle(cards);
            shuffledActionOrder.addAll(cards);
        }

        return shuffledActionOrder;
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
