package com.app.flashcardapp;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;

public class FlashcardCell extends ListCell<Flashcard> {

    @Override
    protected void updateItem(Flashcard flashcard, boolean empty) {
        super.updateItem(flashcard, empty);

        if (empty || flashcard == null) {
            setText(null);
        } else {
            // Get the index of the current item in the ListView
            int index = getIndex();

            // Set the text of the cell to display the index and question of the flashcard
            setText((index + 1) + ". " + flashcard.getQuestion());
        }
    }
}

/* Create a custom cell factory class that extends ListCell<Flashcard>. This class will be responsible for rendering each item in the list view.*/





