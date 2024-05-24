package models;


public abstract class PawCard implements Card{

    private User User;
    private String name;
    private int attack;
    private int agility;
    private int speed;
    private int life;
    private int elixirCost;
    private int rarity; // 1 to 5
    private boolean alive;
    private boolean onTheField = false;


    public PawCard(User user, String name){
        this.User = user;
        this.name = name;
    }

    public int attackEnemy(PawCard enemyCard){
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
                setAlive(false);
                setOnTheField(false);
                System.out.println(String.format("%s is DEAD", getName()));
            }
        }
    }
    
    public void positionateCard(){
        if (!(isOnTheField())){
            if (!(getUser().getElixir() < getElixirCost())){
                getUser().setElixir(getElixirCost() - getElixirCost());
                setOnTheField(true);
                setAlive(true);
                System.out.println(String.format("%s Positionated", getName()));
            } else{
                System.out.println(getUser().getName()+" doesn't have enought Elixir to positionate "+ getName());
            }
        }
    }

    @Override
    public void showCardDetails() {
        System.out.println(String.format("=== %s - %s ===", User.getName(), getName()));
        System.out.println(String.format("HP: %s", getLife()));
        System.out.println(String.format("Attack: %s", getAttack()));
        System.out.println(String.format("Agility: %s", getAgility()));
        System.out.println(String.format("Speed: %s", getAgility()));
        System.out.println(String.format("Rarity: %s", getRarity()));
        System.out.println(String.format("Elixir Cost: %s", getElixirCost()));


    // private int rarity; // 1 to 5
    // private boolean onTheField = false;

        System.out.println("\n");
    }
    
    abstract public void usePower();


    // Getters & Setters
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    };

    @Override
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

    public int getspeed() {
        return speed;
    }

    public void setspeed(int speed) {
        this.speed = speed;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int d) {
        this.life = d;
    }

    @Override
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

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}