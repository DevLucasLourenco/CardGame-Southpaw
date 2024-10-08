package service.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import models.characters.pawbase.PawCard;
import models.characters.pawbase.managePaws;
import models.contracts.Card;
import models.users.User;

public class eventDeal {
    Scanner scanner = new Scanner(System.in);
    protected String GameName = "SouthPaw";
    protected List<User> users = new ArrayList<>();

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
                    String userInfo = String.format("| %-15s | %-7d | %-98s |\n",
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
                table.append("------------------------------\n");

                String textInstructions = String.format("%s, you must select one of the options below to procede", user.getName());
                table.append(textInstructions);

                String textChoice = "\nA) Select a Paw to invoke\nB) Check General States";
                table.append(textChoice);
        
                table.append("\n------------------------------\n");
                System.out.println(table);

                selectionChoice = scanner.nextLine().toUpperCase();
                switch (selectionChoice) {
                    case "A":
                        PawCard paw = ChooseMonsterToInvoke(user);
                        paw.positionateCard();
                        break;
                    case "B":
                        generalBattleState();
                        break;
                    default:
                        break;
                }

                System.out.println("\nDo you want to do something else? (Y/N)");
                proceed = scanner.nextLine().toUpperCase();
                
            } while (!proceed.equals("N"));
        }
    }


    public PawCard ChooseMonsterToInvoke(User user){
        int count = 1;
        Map<Integer, Class<?extends PawCard>> assimilationOrderedPaws = new HashMap<>();

        managePaws managepaws = new managePaws();
        List<Class<? extends PawCard>> availablePaws = managepaws.getPawsAvailableForUsage();
        
        System.out.println("Which Paw do you want to invoke?");
        for (Class<? extends PawCard> card : availablePaws){
            System.out.println(String.format("%d) %s", count, card.getName()));
            
            assimilationOrderedPaws.put(count, card);
            count ++;
        }
        System.out.println(assimilationOrderedPaws);
        Integer res = scanner.nextInt();

        try{
            Class<? extends PawCard> chosenClass = assimilationOrderedPaws.get(res);
            PawCard cardReturn = chosenClass.getDeclaredConstructor(User.class).newInstance(user);
    
            return cardReturn;

        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean ChooseMenu(User user){
        String choice = "";

        ActionBy action = new ActionBy(choice);
        action.run();


        
        return true;
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
