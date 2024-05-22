
public abstract class Card{

    private User User;
    private String name;
    private int attack;
    private int agility;
    private int walkVelocity;
    private int life;
    private int elixirCost;
    private int rarity; // 1 to 5
    public Power powerEnviroment;
    private boolean onTheField = false;


    public Card(User user, String name){
        this.User = user;
        setName(name);
    }

    public int attackEnemy(Card enemyCard){
        if (isOnTheField()){
            enemyCard.setLife(enemyCard.getLife() - this.attack);
            return enemyCard.getLife();
        }
        return 0;
    }

    public int receiveDamage(Card enemyCard){
        if (isOnTheField()){
            int newLifeState = getLife() - enemyCard.getAttack();
            setLife(newLifeState);
            this.dyingState();
            
            return newLifeState;
        }
        return 0;
    }
    
    private void dyingState(){
        if (isOnTheField()){
            if (getLife() <= 0) {
                System.out.println("Died");
                setOnTheField(false);
            }
        }
    }
    
    public void positionateCard(){
        if (!(isOnTheField())){
            if (!(getUser().getElixir() < getElixirCost())){
                getUser().setElixir(getElixirCost() - getElixirCost());
                setOnTheField(true);
                System.out.println(String.format("%s Positionated", getName()));
            }
            System.out.println(getUser().getName()+" doesn't have enought Elixir to positionate "+ getName());
        }
    }
    
    public void usePower(){
        this.powerEnviroment.Use();
    }


    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    };

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getWalkVelocity() {
        return walkVelocity;
    }

    public void setWalkVelocity(int walkVelocity) {
        this.walkVelocity = walkVelocity;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getElixirCost() {
        return elixirCost;
    }

    public void setElixirCost(int elixirCost) {
        this.elixirCost = elixirCost;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User User) {
        this.User = User;
    }

    public boolean isOnTheField() {
        return onTheField;
    }

    public void setOnTheField(boolean onTheField) {
        this.onTheField = onTheField;
    }
}