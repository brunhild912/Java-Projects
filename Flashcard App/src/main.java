public class main {
    public static void main(String[] args){
        flashcard flash1 = new flashcard("Java current version is?", "5");
        flashcard flash2 = new flashcard("Is Java still very popular?", "Yes");

        Deck deck1 = new Deck("Java");
        deck1.addFlashcards(flash1);
        deck1.addFlashcards(flash2);

        deck1.getFlashcards();
    }
}
