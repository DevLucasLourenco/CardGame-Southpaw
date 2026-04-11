package game;

import java.util.ArrayList;
import java.util.List;
import models.characters.pawbase.PawCard;
import models.contracts.Card;
import models.contracts.HasPower;
import models.users.User;
import service.event.By;
import service.event.ShiftDeal;

/**
 * Headless battle controller â€” Yu-Gi-Oh style.
 * Win condition: reduce the opponent's playerHP to 0.
 * Field limit: 5 monsters per player (enforced in PawCard.positionateCard).
 *
 * Damage flow:
 *   â€¢ Monster vs Monster  â†’ damage to the target card's HP
 *   â€¢ Monster vs Player   â†’ damage goes directly to player HP (when field is empty)
 */
public class BattleEngine {

    private final User user1;
    private final User user2;

    private List<Card> actionOrder = new ArrayList<>();
    private int  turnIndex = 0;
    private int  round     = 0;
    private boolean gameOver = false;
    private User    winner   = null;

    /** Stores the last log message so screens can display it. */
    private String lastLog = "";

    public BattleEngine(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        startNextRound();
    }

    // â”€â”€ Round management â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public void startNextRound() {
        round++;
        ShiftDeal shift = new ShiftDeal(user1, user2);
        List<Card> seq  = shift.iterThroughtBy(By.AGILITY);
        actionOrder     = shift.actionOrderingByAgility(seq);
        turnIndex       = 0;
        skipDeadCards();
    }

    public boolean isRoundOver() {
        return turnIndex >= actionOrder.size();
    }

    public int getRound() { return round; }

    // â”€â”€ Current turn â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public PawCard getCurrentCard() {
        if (isRoundOver()) return null;
        Card c = actionOrder.get(turnIndex);
        return (c instanceof PawCard) ? (PawCard) c : null;
    }

    public User getCurrentOwner() {
        PawCard c = getCurrentCard();
        return c == null ? null : c.getUser();
    }

    public User getOpponent(User owner) {
        return owner == user1 ? user2 : user1;
    }

    // â”€â”€ State â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    public boolean isGameOver()  { return gameOver;   }
    public User    getWinner()   { return winner;      }
    public User    getUser1()    { return user1;       }
    public User    getUser2()    { return user2;       }
    public String  getLastLog()  { return lastLog;     }

    // â”€â”€ Actions â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Attacks a specific enemy paw.
     * Returns damage dealt to the target card.
     */
    public int attack(PawCard attacker, PawCard target) {
        int before = target.getLife();
        attacker.attackEnemy(target);
        int dealt  = Math.max(0, before - target.getLife());
        attacker.getUser().getStatistic().addDamageDealt(dealt);
        if (!target.isOnTheField()) {
            attacker.getUser().getStatistic().incrementPawsDefeated();
            lastLog = attacker.getName() + " destroyed " + target.getName() + "!";
        } else {
            lastLog = attacker.getName() + " hit " + target.getName() + " for " + dealt;
        }
        checkGameOver();
        advanceTurn();
        return dealt;
    }

    /**
     * Direct attack on the opponent player (no paws on their field).
     * Deals attacker's ATK directly to the opponent's playerHP.
     * Returns damage dealt.
     */
    public int attackPlayer(PawCard attacker) {
        User opponent = getOpponent(attacker.getUser());
        int  dealt    = attacker.attackPlayer(opponent);
        attacker.getUser().getStatistic().addDirectDamage(dealt);
        lastLog = attacker.getName() + " attacked " + opponent.getName()
                + " directly! (-" + dealt + " HP)";
        checkGameOver();
        advanceTurn();
        return dealt;
    }

    /** Uses the card's special power. Returns false if the card has no power. */
    public boolean usePower(PawCard card) {
        if (!(card instanceof HasPower)) return false;
        ((HasPower) card).usePower();
        card.getUser().getStatistic().incrementPowersUsed();
        lastLog = card.getName() + " used their power!";
        checkGameOver();
        advanceTurn();
        return true;
    }

    /**
     * Summons a new paw for the given user.
     * Respects elixir cost and the 5-monster field limit.
     */
    public boolean summon(User owner, Class<? extends PawCard> cardClass) {
        try {
            PawCard paw = cardClass.getDeclaredConstructor(User.class).newInstance(owner);
            if (!owner.hasRoom()) {
                lastLog = "Field full! Cannot summon " + cardClass.getSimpleName();
                return false;
            }
            paw.positionateCard();
            lastLog = owner.getName() + " summoned " + paw.getName() + "!";
            return paw.isOnTheField();
        } catch (Exception e) {
            return false;
        }
    }

    /** Passes the current card's turn. */
    public void pass() {
        PawCard c = getCurrentCard();
        lastLog = (c != null ? c.getName() : "?") + " passed.";
        advanceTurn();
    }

    // â”€â”€ Internal â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void advanceTurn() {
        turnIndex++;
        skipDeadCards();
    }

    private void skipDeadCards() {
        while (turnIndex < actionOrder.size()) {
            Card c = actionOrder.get(turnIndex);
            if (c instanceof PawCard && !((PawCard) c).isOnTheField()) {
                turnIndex++;
            } else {
                break;
            }
        }
    }

    private void checkGameOver() {
        for (User u : new User[]{ user1, user2 }) {
            if (!u.isAlive()) {
                gameOver = true;
                winner   = getOpponent(u);
                return;
            }
        }

    }
}
