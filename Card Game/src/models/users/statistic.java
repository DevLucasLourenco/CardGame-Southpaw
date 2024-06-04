package models.users;


public class Statistic {
    protected User user;
    protected int pawsDefeated;
    protected int totalDamageInflicted;
    

    public Statistic(User user){
        this.user = user;
    }

    @Override
    public String toString() {
        return "Statistic []";
    }
    
}