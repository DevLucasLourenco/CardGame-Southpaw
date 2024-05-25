package service.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Card;
import models.User;

public class shiftDeal {

    public List<User> userList= new ArrayList<User>();

    public shiftDeal(User user1, User user2) {
        this.userList.add(user1);
        this.userList.add(user2);
    }

    public Map<User, List<Card>> iterThroughtBy(String option){
        Map<User, List<Card>> result = new HashMap<>();

        switch (option.toLowerCase()) {
            case "speed":
                result = __speed();
                break;
        
            case "attack":
                result = __attack();
                break;
        }
        return result;
    }

    private Map<User, List<Card>> __speed(){
        
    }

    private Map<User, List<Card>> __attack(){

    }
}
