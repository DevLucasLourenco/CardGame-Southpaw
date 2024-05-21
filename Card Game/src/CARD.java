
abstract class CARD{

    private User User;
    private String name;
    private int attack;
    private int agility;
    private int walkVelocity;
    private int life;
    private int elixirCost;
    private int rarity; // 1 to 5
    public PowerEnviroment powerEnviroment;
    private boolean onTheField = false;


    public CARD(User user){
        this.User = user;
    }

    public int attackEnemy(CARD enemyCard){
        if (isOnTheField()){ // fazer o mesmo para todos os metodos
            enemyCard.setLife(enemyCard.getLife() - this.attack);
            return enemyCard.getLife();
        }
        return 0;
    }

    public int receiveDamage(CARD enemyCard){
        int damageReceived = getLife() - enemyCard.getAttack();
        setLife(damageReceived);
        
        return damageReceived;
    }
    
    public void dyingState(){
        if (getLife() <= 0) {
            System.out.println("Died");
            setOnTheField(false);
        }
    }
    
    public void positionateCard(){
        if (!(getUser().getElixir() < getElixirCost())){
            getUser().setElixir(getElixirCost() - getElixirCost());
            setOnTheField(true);
            System.out.println(String.format("%s Positionated", getName()));
        }
        System.out.println(getUser().getName()+" doesn't have enought Elixir to positionate "+ getName());
    }
    
    public void usePower(){
        this.powerEnviroment.Use();
    }


    // Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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