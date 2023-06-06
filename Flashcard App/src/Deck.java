import java.util.ArrayList;
import java.util.List;

public class Deck {
    String title;
    List<flashcard> flashcardsList;

    /* --- CONSTRUCTOR --- */
    public Deck(String title){
        this.title = title;
        this.flashcardsList = new ArrayList<>();
    }

    /* --- GETTERS --- */

    public String getTitle() {
        return title;
    }

    public void addFlashcards(flashcard flashcard){
        flashcardsList.add(flashcard);
    }

    public List<flashcard> getFlashcards() {
        return flashcardsList;
    }


}
