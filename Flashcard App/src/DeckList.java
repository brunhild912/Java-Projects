import java.util.ArrayList;
import java.util.List;

public class DeckList {
    private List<Deck> decks;

    public DeckList() {
        this.decks = new ArrayList<>();
    }

    public void addDeck(Deck deck) {
        decks.add(deck);
    }

    public List<Deck> getDecks() {
        return decks;
    }

    // Add any additional methods or functionality as needed
}

