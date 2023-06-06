package com.app.flashcardapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Flashcard {
    @JsonProperty("question")
    private String question;

    @JsonProperty("answer")
    private String answer;

    private boolean seen;
    private Deck deck;

    /* --- CONSTRUCTOR --- */
    @JsonCreator
    public Flashcard(){
        // Default constructor for JSON
    }

    public Flashcard(String question, String answer){
        this.question = question;
        this.answer = answer;
        this.seen = false;
    }

    /* --- METHODS --- */
    public void flip(){
        seen = !seen;
    }

    public void markAsSeen(){
        seen = true;
    }

    public void markAsUnseen(){
        seen = false;
    }

    public void resetCard() {
        seen = false;
        // Additional logic to reset any other properties or states of the flashcard
    }

    @Override
    public String toString() {
        return "Question: " + question + "\nAnswer: " + answer;
    }

    /* --- GETTERS --- */

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Deck getDeck() {
        return deck;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setQuestion(String question){
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
