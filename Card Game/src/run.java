public class run {
    public static void main(String[] args) {
        User user1 = new User();
        user1.setName("Lucas");
        Card card = new Pawarrior(user1);
        System.out.println(card.getName());

    }
    
}
