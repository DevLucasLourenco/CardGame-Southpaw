package models.users;


public class statistic {
    protected User user;
    protected int pawsDefeated;
    protected int totalDamageInflicted;
    

    public statistic(User user){
        this.user = user;
    }

    @Override
    public String toString() {
        return "Statistic []";
    }
    
}