package service.battle.against;

import java.util.List;
import java.util.Scanner;
import models.characters.pawbase.PawCard;
import models.contracts.Card;
import models.users.User;
import service.event.By;
import service.event.EventDeal;
import service.event.ShiftDeal;

public class Battle {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ── Banner & Player Names ──────────────────────────────
        System.out.println("Set player info.\n");
        System.out.println("FIRST player name:");
        String firstName  = scanner.nextLine().trim().toUpperCase();

        System.out.println("SECOND player name:");
        String secondName = scanner.nextLine().trim().toUpperCase();

        User user1 = new User(firstName);
        User user2 = new User(secondName);

        // ── Event engine (shares the same Scanner to avoid buffer issues) ──
        EventDeal event = new EventDeal(scanner);
        event.insertUsers(user1, user2);

        // ── Pre-battle: each player builds their team ──────────
        System.out.println("\n=== PRE-BATTLE SETUP ===");
        System.out.println("(R) View rules before starting? (Y/N)");
        if (scanner.nextLine().trim().toUpperCase().equals("Y")) {
            event.showRules();
        }

        event.FirstMenu();

        // ── Require at least 1 paw per player ─────────────────
        if (user1.getPawUnderControl().isEmpty() || user2.getPawUnderControl().isEmpty()) {
            System.out.println("\nBoth players must have at least 1 Paw on the field to start. Exiting.");
            scanner.close();
            return;
        }

        System.out.println("\n=== BATTLE START ===");
        event.generalBattleState();

        // ── Game Loop ──────────────────────────────────────────
        boolean gameOver = false;
        int round = 0;

        while (!gameOver) {
            round++;
            System.out.printf("%n╔══════════════════════════════════╗%n");
            System.out.printf( "║           ROUND  %-3d              ║%n", round);
            System.out.printf( "╚══════════════════════════════════╝%n");

            // Recalculate turn order each round (reflects deaths from previous round)
            ShiftDeal shiftdeal = new ShiftDeal(user1, user2);
            List<Card> sequence    = shiftdeal.iterThroughtBy(By.AGILITY);
            List<Card> actionOrder = shiftdeal.actionOrderingByAgility(sequence);

            if (actionOrder.isEmpty()) {
                System.out.println("No cards on the field. It's a draw!");
                break;
            }

            for (Card card : actionOrder) {
                if (!(card instanceof PawCard)) continue;
                PawCard pawCard = (PawCard) card;

                // Card may have died earlier in this same round
                if (!pawCard.isOnTheField()) continue;

                User owner    = pawCard.getUser();
                gameOver = event.cardTurnMenu(pawCard, owner);
                if (gameOver) break;
            }

            // Between rounds, show state if game continues
            if (!gameOver) {
                System.out.println("\n--- End of Round " + round + " ---");
                event.generalBattleState();

                // Check for stalemate (both players have no paws)
                if (user1.getPawUnderControl().isEmpty() && user2.getPawUnderControl().isEmpty()) {
                    System.out.println("Both players are out of Paws. It's a DRAW!");
                    event.printFinalStats();
                    gameOver = true;
                }
            }
        }

        scanner.close();
    }
}
