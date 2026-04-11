package service.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import models.characters.pawbase.PawCard;
import models.characters.pawbase.ManagePaws;
import models.contracts.Card;
import models.contracts.HasPower;
import models.users.User;

public class EventDeal {
    private Scanner scanner;
    protected String GameName = "SouthPaw";
    protected List<User> users = new ArrayList<>();

    public EventDeal() {
        this.scanner = new Scanner(System.in);
        inicializationFirstEvent();
    }

    public EventDeal(Scanner scanner) {
        this.scanner = scanner;
        inicializationFirstEvent();
    }

    public void inicializationFirstEvent() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║           S O U T H P A W        ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.println("          Good Luck!\n");
    }

    public void insertUsers(User... users) {
        this.users.addAll(Arrays.asList(users));
    }

    // ────────────────────────────────────────────────────────────
    // BATTLE STATE DISPLAY
    // ────────────────────────────────────────────────────────────

    public void generalBattleState() {
        if (this.users.isEmpty()) {
            System.out.println("No users available. Try to set them.");
            return;
        }
        StringBuilder table = new StringBuilder();
        String separator = "+------------------+---------+----------------------------------------------------------------------------------------------------+\n";
        String header    = String.format("| %-16s | %-7s | %-98s |%n", "Player", "Elixir", "Paws on Field");

        table.append(separator).append(header).append(separator);

        for (User user : this.users) {
            List<Card> cards = user.getPawUnderControl();
            if (cards.isEmpty()) {
                table.append(String.format("| %-16s | %-7d | %-98s |%n",
                        user.getName(), user.getElixir(), "--- no paws on field ---"));
            } else {
                for (int i = 0; i < cards.size(); i++) {
                    String playerCol  = (i == 0) ? user.getName() : "";
                    String elixirCol  = (i == 0) ? String.valueOf(user.getElixir()) : "";
                    table.append(String.format("| %-16s | %-7s | %-98s |%n",
                            playerCol, elixirCol, cards.get(i).exportInfo()));
                }
            }
            table.append(separator);
        }
        System.out.println(table);
    }

    // ────────────────────────────────────────────────────────────
    // PRE-BATTLE SETUP MENU
    // ────────────────────────────────────────────────────────────

    public void FirstMenu() {
        for (User user : this.users) {
            System.out.printf("%n--- %s, prepare your team! (Elixir: %d) ---%n",
                    user.getName(), user.getElixir());
            String proceed;
            do {
                System.out.println("\nA) Summon a Paw  (spends Elixir)");
                System.out.println("B) View current state");
                System.out.print("> ");

                String choice = scanner.nextLine().toUpperCase().trim();
                switch (choice) {
                    case "A":
                        PawCard paw = ChooseMonsterToInvoke(user);
                        if (paw != null) paw.positionateCard();
                        break;
                    case "B":
                        generalBattleState();
                        break;
                    default:
                        System.out.println("Invalid option.");
                }

                System.out.printf("%nSummon another Paw? (Y/N) [Elixir: %d]%n> ", user.getElixir());
                proceed = scanner.nextLine().toUpperCase().trim();
            } while (proceed.equals("Y") && user.getElixir() > 0);
        }
    }

    public PawCard ChooseMonsterToInvoke(User user) {
        int count = 1;
        Map<Integer, Class<? extends PawCard>> indexedPaws = new HashMap<>();

        ManagePaws registry = new ManagePaws();
        List<Class<? extends PawCard>> available = registry.getPawsAvailableForUsage();

        System.out.println("\nAvailable Paws (your Elixir: " + user.getElixir() + "):");
        for (Class<? extends PawCard> cls : available) {
            try {
                // Create a temp instance just to read stats — we use a throw-away user
                PawCard preview = cls.getDeclaredConstructor(User.class).newInstance(user);
                System.out.printf("  %d) %s%n", count, preview.exportInfo());
            } catch (Exception e) {
                System.out.printf("  %d) %s%n", count, cls.getSimpleName());
            }
            indexedPaws.put(count, cls);
            count++;
        }

        int res = -1;
        while (res < 1 || res >= count) {
            System.out.print("Enter number (or 0 to cancel): ");
            try {
                res = Integer.parseInt(scanner.nextLine().trim());
                if (res == 0) return null;
                if (res < 1 || res >= count) System.out.println("Invalid number.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        try {
            Class<? extends PawCard> chosen = indexedPaws.get(res);
            return chosen.getDeclaredConstructor(User.class).newInstance(user);
        } catch (Exception e) {
            System.out.println("Failed to create paw: " + e.getMessage());
        }
        return null;
    }

    // ────────────────────────────────────────────────────────────
    // BATTLE TURN SYSTEM
    // ────────────────────────────────────────────────────────────

    /**
     * Handles one card's full turn.
     * Actions: Attack | Use Power | Summon a new Paw | View state | Pass
     *
     * @return true if the game has ended (a player's Elixir hit 0)
     */
    public boolean cardTurnMenu(PawCard card, User owner) {
        if (!card.isOnTheField()) return false;

        User opponent = getOpponent(owner);
        if (opponent == null) return false;

        System.out.println("\n══════════════════════════════════════════");
        System.out.printf( "  %s's turn  |  acting: %s%n", owner.getName(), card.getName());
        System.out.println("══════════════════════════════════════════");
        card.getCardDetails(true);

        boolean hasPow     = card instanceof HasPower;
        boolean actionDone = false;

        while (!actionDone) {
            System.out.println("\nWhat will " + card.getName() + " do?");
            System.out.println("  A) Attack");
            if (hasPow) System.out.println("  B) Use Power");
            System.out.println("  C) Summon a new Paw  (spends Elixir)");
            System.out.println("  D) View Battle State");
            System.out.println("  E) Pass");
            System.out.print("> ");

            String choice = scanner.nextLine().toUpperCase().trim();
            switch (choice) {
                case "A":
                    executeAttack(card, opponent);
                    actionDone = true;
                    break;
                case "B":
                    if (hasPow) {
                        ((HasPower) card).usePower();
                        owner.getStatistic().incrementPowersUsed();
                        actionDone = true;
                    } else {
                        System.out.println("This card has no power.");
                    }
                    break;
                case "C":
                    PawCard newPaw = ChooseMonsterToInvoke(owner);
                    if (newPaw != null) newPaw.positionateCard();
                    actionDone = true;
                    break;
                case "D":
                    generalBattleState();
                    break;
                case "E":
                    System.out.println(card.getName() + " passes.");
                    actionDone = true;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        return checkGameOver();
    }

    private void executeAttack(PawCard attacker, User opponent) {
        List<Card> targets = new ArrayList<>(opponent.getPawUnderControl());

        if (targets.isEmpty()) {
            // ── Direct Player Attack ──────────────────────────────
            int damage     = attacker.getElixirCost();
            int newElixir  = Math.max(0, opponent.getElixir() - damage);
            opponent.setElixir(newElixir);
            attacker.getUser().getStatistic().addDirectDamage(damage);

            System.out.printf("%n  >>> %s attacks %s DIRECTLY!  -%d Elixir  (%s now has %d Elixir)%n",
                    attacker.getName(), opponent.getName(), damage,
                    opponent.getName(), newElixir);
        } else {
            // ── Target Selection ──────────────────────────────────
            System.out.println("\nChoose a target:");
            for (int i = 0; i < targets.size(); i++) {
                System.out.printf("  %d) %s%n", i + 1, targets.get(i).exportInfo());
            }

            int choice = -1;
            while (choice < 0 || choice >= targets.size()) {
                System.out.print("Enter number: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    if (choice < 0 || choice >= targets.size())
                        System.out.println("Invalid target.");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                    choice = -1;
                }
            }

            PawCard target   = (PawCard) targets.get(choice);
            boolean wasAlive = target.isAlive();
            int remaining    = attacker.attackEnemy(target);

            attacker.getUser().getStatistic().addDamageDealt(attacker.getAttack());
            System.out.printf("%n  >>> %s attacks %s!  Remaining HP: %d%n",
                    attacker.getName(), target.getName(), remaining);

            if (wasAlive && !target.isAlive()) {
                System.out.printf("  *** %s was defeated! ***%n", target.getName());
                attacker.getUser().getStatistic().incrementPawsDefeated();
            }
        }
    }

    // ────────────────────────────────────────────────────────────
    // HELPERS
    // ────────────────────────────────────────────────────────────

    private User getOpponent(User user) {
        for (User u : users) {
            if (!u.equals(user)) return u;
        }
        return null;
    }

    /**
     * Checks every user's Elixir. If any player is at 0, prints game-over and returns true.
     */
    public boolean checkGameOver() {
        for (User u : users) {
            if (u.getElixir() <= 0) {
                u.setElixir(0);
                User winner = getOpponent(u);
                System.out.println("\n╔══════════════════════════════════╗");
                System.out.println("║           G A M E  O V E R       ║");
                System.out.printf( "║  %s ran out of Elixir!%n", u.getName());
                if (winner != null)
                    System.out.printf("║  >>> %s WINS! <<<%n", winner.getName());
                System.out.println("╚══════════════════════════════════╝");
                printFinalStats();
                return true;
            }
        }
        return false;
    }

    public void printFinalStats() {
        System.out.println("\n=== Final Statistics ===");
        for (User u : users) {
            System.out.printf("  %s: %s%n", u.getName(), u.getStatistic());
        }
    }

    public void showRules() {
        System.out.println("\n=== SOUTHPAW RULES ===");
        System.out.println("• Each player starts with 10 Elixir.");
        System.out.println("• Summoning a Paw costs its Elixir Cost.");
        System.out.println("• Using a Power also costs the card's Elixir Cost.");
        System.out.println("• When you attack a player who has no Paws, they lose Elixir equal to the attacker's Elixir Cost.");
        System.out.println("• A player loses when their Elixir reaches 0 — from attacks OR from summoning.");
        System.out.println("• Turn order is determined by Agility (higher = acts first and more often).");
    }
}
