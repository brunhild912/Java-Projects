package com.app.flashcardapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
/*  the @JsonCreator annotation is used on the constructor to indicate that it should be used for deserialization. The @JsonProperty annotation is used to specify the mapping between the JSON property name and the constructor parameter.

Make sure that the parameter name in the @JsonProperty annotation matches the key in your JSON structure. */

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Deck {
    String name;
    List<Flashcard> FlashcardsList;

    List<Flashcard> flashcards;

    /* --- CONSTRUCTOR --- */
    @JsonCreator
    public Deck (@JsonProperty("name")String name){
        this.name = name;
        FlashcardsList = new ArrayList<>();
    }

    public List<Flashcard> getFlashcards() {
        return flashcards;
    }

    //add flash cards
    public void addFlashcards(Flashcard flashcard){
        FlashcardsList.add(flashcard);
    }

    //remove flashcards
    public void deleteFlashcards(Flashcard flashcard){
        FlashcardsList.remove(flashcard);
    }

    //shuffle flashcards
    public void shuffleFlashcards(){
        Collections.shuffle(FlashcardsList);
    }

    /* --- GETTERS --- */

    public String getName() {
        return name;
    }

    public List<Flashcard> getFlashcardsList() {
        return FlashcardsList;
    }

    // Override toString() method to return deck name
    @Override
    public String toString() {
        return name;
    }
}