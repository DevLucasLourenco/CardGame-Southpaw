package service.event;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Card;
import models.User;

public class Party{

    private Map<User, List<Card>> partyPerUser;
    
    
    public void insertUsers(User user1, User user2){
        partyDeal partyLead = new partyDeal(user1, user2);
        partyLead.mappingCards();
        this.partyPerUser = partyLead.getParty();
    }
    

    // Getters &  Setters
    public Map<User, List<Card>> getPartyPerUser() {
        return partyPerUser;
    }
}

class partyDeal {

    // private Map<User, List<Card>> party = new
    public List<User> userList= new ArrayList<User>();
    public Map<User, List<Card>> partyMap = new HashMap<>();

    public partyDeal(User user1, User user2) {
        this.userList.add(user1);
        this.userList.add(user2);
    }

    public void mappingCards(){
        for (int i=0; i < this.userList.size(); i++){
            User currentUser = this.userList.get(i);
            this.partyMap.put(currentUser, currentUser.getPawUnderControl());
        }
    }
        
        public Map<User, List<Card>> getParty(){
            return this.partyMap;
            
            
        }
        
}
    

