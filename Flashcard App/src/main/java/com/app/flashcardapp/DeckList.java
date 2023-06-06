package com.app.flashcardapp;

import java.util.ArrayList;
import java.util.List;

public class DeckList {
    List<Deck> decksList;

    public DeckList(){
        decksList = new ArrayList<>();
    }

    public void addDeck(Deck deck){
        decksList.add(deck);
    }

    public List<Deck> getDecksList() {
        return decksList;
    }
}

