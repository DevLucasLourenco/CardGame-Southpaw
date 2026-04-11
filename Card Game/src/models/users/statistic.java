package models.users;

public class Statistic {
    protected User user;
    protected int pawsDefeated;
    protected int totalDamageDealt;
    protected int totalDirectDamage;
    protected int powersUsed;

    public Statistic(User user) {
        this.user = user;
    }

    public void incrementPawsDefeated() {
        this.pawsDefeated++;
    }

    public void addDamageDealt(int dmg) {
        this.totalDamageDealt += dmg;
    }

    public void addDirectDamage(int dmg) {
        this.totalDirectDamage += dmg;
    }

    public void incrementPowersUsed() {
        this.powersUsed++;
    }

    // Getters
    public int getPawsDefeated()      { return pawsDefeated; }
    public int getTotalDamageDealt()  { return totalDamageDealt; }
    public int getTotalDirectDamage() { return totalDirectDamage; }
    public int getPowersUsed()        { return powersUsed; }

    @Override
    public String toString() {
        return String.format(
            "Paws Defeated: %d | Damage Dealt: %d | Direct Damage Dealt: %d | Powers Used: %d",
            pawsDefeated, totalDamageDealt, totalDirectDamage, powersUsed
        );
    }
}
