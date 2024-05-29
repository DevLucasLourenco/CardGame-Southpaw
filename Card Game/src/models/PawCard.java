package models;


public abstract class PawCard implements Card{

    private User User;
    private String name;
    private String nickName;
    private int attack;
    private int agility;
    private int life;
    private int maxLife;
    private int elixirCost;
    private int rarity; // 1 to 5
    
    private boolean alive;
    private boolean onTheField = false;


    public PawCard(User user){
        // constructor without nickname
        this.User = user;
        this.name = getClass().getSimpleName();
        setCardDetails();
    }
    
    public PawCard(User user, String nickname){
        // constructor with nickname
        this.User = user;
        this.name = getClass().getSimpleName();
        if (nickname.length()<= 15){
            this.nickName = nickname;
        } else{
            System.out.println("The lenght of the nickname must be below to 15 characters");
        }
        setCardDetails();
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
                getUser().getPawUnderControl().remove(this);
                System.out.println(String.format("%s is DEAD", getName()));
            }
        }
    }
    
    public void positionateCard(){
        if (!(isOnTheField())){
            if (!(getUser().getElixir() < getElixirCost())){
                getUser().setElixir(getUser().getElixir() - getElixirCost());
                setOnTheField(true);
                setAlive(true);
                getUser().getPawUnderControl().add(this);
                System.out.println(String.format("%s Positionated (%s)", getName(), getUser().getName()));
            } else{
                System.out.println(getUser().getName()+" doesn't have enought Elixir to positionate "+ getName());
            }
        }
    }

    @Override
    public String getCardDetails(boolean print) {
        String texto = String.format("| === %s - %s=== \n", User.getName(), getName())+
        String.format("|HP: %s/%s\n",getMaxLife() , getLife())+
        String.format("|Attack: %s\n", getAttack())+
        String.format("|Agility: %s\n", getAgility())+
        String.format("|Rarity: %s\n", getRarity())+
        String.format("|Elixir Cost: %s", getElixirCost());

        if (print){
            System.out.println(texto);
            System.out.println("\n");
        }

        return texto;
    }
    
    @Override
    public String toString() {
        return String.format(
            "| %-15s | HP: %6d/%-6d| Attack: %-4d| Agility: %-2d| Rarity: %-2d| Elixir Cost: %-2d",
            getName(), getMaxLife(), getLife(), getAttack(), getAgility(), getRarity(), getElixirCost()
        );
    }

    abstract public void usePower();


    // Getters & Setters
    @Override
    public String getName() {
        if (getNickName() != null){
            return this.nickName;
        }
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

    @Override
    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int d) {
        this.life = d;
        if (getMaxLife() == 0){
            this.maxLife = this.life;
        }
        dyingState();
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

    @Override
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getMaxLife() {
        return maxLife;
    }
}