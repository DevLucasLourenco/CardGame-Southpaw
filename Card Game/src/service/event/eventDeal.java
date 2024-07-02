package service.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import models.characters.Pawarrior;
import models.characters.Pawclown;
import models.characters.Pawskeleton;
import models.contracts.Card;
import models.users.User;

public class eventDeal {
    Scanner scanner = new Scanner(System.in);
    protected String GameName = "SouthPaw";
    protected List<User> users = new ArrayList<>();;

    {
        inicializationFirstEvent();
    }

    public void inicializationFirstEvent() {
        System.out.println(String.format("===== %s =====", this.GameName));
        System.out.println(String.format("Good Luck!", this.GameName));
    }

    public void insertUsers(User... users) {
        this.users.addAll(Arrays.asList(users));
    }

    public void generalBattleState() {
        if (!this.users.isEmpty()) {
            StringBuilder table = new StringBuilder();
            String header = String.format("| %-15s | %-7s | %-98s |\n", "User", "Elixir", "Paws");
            String separator = "|-----------------|---------|----------------------------------------------------------------------------------------------------|\n";

            table.append(separator);
            table.append(header);
            table.append(separator);

            for (User user : this.users) {
                List<Card> cards = user.getPawUnderControl();
                if (cards.isEmpty()) {
                    String userInfo = String.format("| %-15s | %-7d | %-100s |\n",
                            user.getName(), user.getElixir(), "No paws controlled");
                    table.append(userInfo);
                } else {
                    String userInfo = String.format("| %-15s | %-7d |", user.getName(), user.getElixir());
                    table.append(userInfo);

                    for (int i = 0; i < cards.size(); i++) {
                        if (i > 0) {
                            table.append(String.format("| %-15s | %-7s |", "", ""));
                        }
                        table.append(String.format(" %-98s |\n", cards.get(i).exportInfo()));
                    }
                }

                table.append(separator);
            }

            System.out.println(table.toString());
        } else {
            System.out.println("No users available. Try to set them.");
        }
    }

    public void showRules() {

    }

    public void FirstMenu() {
        for (User user : this.users){
            String proceed;
            do { 
                String selectionChoice;
                StringBuilder table = new StringBuilder();
                table.append("------------------------------");

                String textInstructions = String.format("%s, you must select one of the options below to procede", user.getName());
                table.append(textInstructions);

                String textChoice = "A) Select a Paw to invoke\nB) Check General States";
                table.append(textChoice);
        
                table.append("------------------------------\n");
                System.out.println(table);

                selectionChoice = scanner.nextLine().toUpperCase();
                switch (selectionChoice) {
                    case "A":
                        ChooseMonsterToInvoke(user);
                        break;
                    case "B":
                        break;
                    default:
                        break;
                }

                // fazer aqui algo c essa escolha

                System.out.println("\nDo you want to do something else?");
                proceed = scanner.nextLine().toUpperCase();
            } while (!proceed.equals("N"));
        }
    }

    public void ChooseMenu() {
        String choice = "";

        ActionBy action = new ActionBy(choice);
        action.run();
    }

    public void ChooseMonsterToInvoke(User user){
        Map<String, Card>PawsLookUpTable = new HashMap<>();
        PawsLookUpTable.put("A", new Pawarrior(user));
        PawsLookUpTable.put("B", new Pawskeleton(user));
        PawsLookUpTable.put("C", new Pawclown(user));


        System.out.println("Which Paw do you want to invoke?\nA) Pawarrior\nB) Pawskeleton\nC) Pawclown");
        String res = scanner.nextLine().toUpperCase();

        PawsLookUpTable.get(res);
    }
}

class ActionBy {
    // Used when an option at Choose Menu has been selected
    protected String ChoosedOption;

    public ActionBy(String ChoosedOption) {
        this.ChoosedOption = ChoosedOption;

    }

    public void run() {

    }
}
