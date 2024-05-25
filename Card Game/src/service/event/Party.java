package service.event;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.Card;
import models.User;

public class Party{

    public Party() {
        
    }
    
    public void insertUsers(User user1, User user2){
        partyLead partyLead = new partyLead(user1, user2);
        partyLead.mappingCards();
        // var party = partylead.getParty();

    }

}

class partyLead {

    // private Map<User, List<Card>> party = new
    public List<User> userList= new ArrayList<User>();
        

    public partyLead(User user1, User user2) {
        this.userList.add(user1);
        this.userList.add(user2);
    }

    public void mappingCards(){
        
        // for (int i=0; i < this.userList.size(); i++){
        //     User user_iterated = this.userList.get(i);
            
        //     for (int j=0; j < user_iterated.getPawUnderControl().size(); j++){
        //         Card pawcard = user_iterated.getPawUnderControl().get(j);
        //         System.out.println(String.format("%s - %s",pawcard.getUser().getName(), pawcard.getName()));
        //     }
        // }

        
    }
        
        // public Map<User, List<Card>> getParty(){
            
            
        //     return  ;
        // }
        
    };
    

