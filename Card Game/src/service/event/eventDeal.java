package service.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.contracts.Card;
import models.users.User;

public class eventDeal {
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
        StringBuilder table = new StringBuilder();
        table.append("------------------------------");
        // String txt = """
        // _______________________________________________
        // | |
        // | |
        // | |
        // | |
        // | |
        // |______________________________________________|
        // """;
    }

    public void ChooseMenu() {
        String choice = "";

        ActionBy action = new ActionBy(choice);
        action.run();
    }
}

class ActionBy {
    // Used when an option at Choose Menu have been selected
    protected String ChoosedOption;

    public ActionBy(String ChoosedOption) {
        this.ChoosedOption = ChoosedOption;

    }

    public void run() {

    }
}
