package service.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Card;
import models.User;

public class shiftDeal {
    private List<User> userList = new ArrayList<User>();
    
    private Map<User, List<Card>> mappedCards = new HashMap<>();
    
    public shiftDeal(Map<User,List<Card>> partyperuser) {
        this.mappedCards = partyperuser;
    }

    public void putUserList(List<User> list){
        this.userList = list;
    }
    
    public Map<Card, User> iterThroughtBy(String option){
        Map<Card, User> result = new HashMap<>();
        
        switch (option.toLowerCase()) {
            case "speed":
                result = __speed();
                break;

            // case "attack":
            //     result = __attack();
            //     break;
        }
        // return ordenateMappedCards(result);
        return result;
    }

    private Map<Card, User> __speed(){
        Map<Card, User> auxMap = new HashMap<>();
        
        for (int i=0; i< this.userList.size(); i++){
            User currentUser = this.userList.get(i);
            System.out.println(currentUser.getName());
            System.out.println(this.mappedCards.get(currentUser));
            
            // for (int j=0; j < currentUser.getPawUnderControl().size(); j++){
                
            // }
        }
        return auxMap;
    }

    // private Map<Card, User> ordenateMappedCards(Map<User, List<Card>> result){

    // }

    // private Map<User, List<Card>> __attack(){

    // }


    // Getters and setters
    public Map<User, List<Card>> getMappedCards() {
        return mappedCards;
    }
    
    public void setMappedCards(Map<User, List<Card>> mappedCards) {
        this.mappedCards = mappedCards;
    }

    public List<User> getUserList() {
        return userList;
    }
    
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
// em python, eu posso utilizar o Literal["opção1", "opção2"] para quando o passar a informação para um método, aparecer a opções designadas. mas e quando for para java? como fazer?