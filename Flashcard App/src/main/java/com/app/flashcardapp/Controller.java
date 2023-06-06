package com.app.flashcardapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller implements Initializable {
    @FXML
    private Button deleteButton;
    @FXML
    private Label titleLabel;

    private static Controller instance;
    public Controller(){
        instance = this;
    }

    public static Controller getInstance() {
        return instance;
    }

    private FlashcardScreenController flashcardScreenController;

    public void setFlashcardScreenController(FlashcardScreenController flashcardScreenController) {
        this.flashcardScreenController = flashcardScreenController;
    }

    @FXML
    private ListView<Deck> deckListView;


    @FXML
    private Button newButton;

    private ObservableList<Deck> deckList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDecks(); // Load decks from storage
        deckListView.setItems(deckList);
        setFlashcardScreenController(flashcardScreenController);
    }

    @FXML
    public void initialize() {

        // Set up the custom style for the ListView
        deckListView.getStyleClass().add("custom-list-view");

        deckListView.setCellFactory(param -> new ListCell<Deck>() {
            @Override
            protected void updateItem(Deck deck, boolean empty) {
                super.updateItem(deck, empty);
                if (empty || deck == null) {
                    setText(null);
                } else {
                    setText(deck.getName());
                }
            }
        });

        deckListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();
                if (selectedDeck != null) {
                    instance.switchToFlashcardScreen(selectedDeck);
                }
            }
        });
    }








    @FXML
    private void handleNewButton(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Deck");
        dialog.setHeaderText("Enter the deck name:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(deckName -> addDeck(deckName));
    }

    public void loadDecks() {
        deckList.clear();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("decks.json");
            if (file.exists()) {
                Deck[] decks = objectMapper.readValue(file, Deck[].class);
                deckList.addAll(decks);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

    private void saveDecks() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("decks.json");
            objectMapper.writeValue(file, deckList.toArray(new Deck[0]));

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

    private void addDeck(String deckName) {
        Deck newDeck = new Deck(deckName);
        deckList.add(newDeck);
        saveDecks();
    }

    /* SWITCHING TO FLASHCARD SCREEN METHODS */
    @FXML
    private void handleDeckSelection(ActionEvent event) {
        Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();
        if (selectedDeck != null) {
            switchToFlashcardScreen(selectedDeck);
        }
    }


    public void switchToFlashcardScreen(Deck deck) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("flashcardScreen.fxml"));
            Parent root = loader.load();

            FlashcardScreenController flashcardScreenController = loader.getController();
            flashcardScreenController.initializeFlashcardScreen(deck);

            Stage stage = new Stage();
            stage.setTitle("Flasho");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Set the close request handler for the new stage
            stage.setOnCloseRequest(event -> {
                flashcardScreenController.closeFlashcardScreen();
                event.consume();
            });

            // Hide the deck screen
            Stage deckScreenStage = (Stage) deckListView.getScene().getWindow();
            deckScreenStage.hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void handleDeckDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();
            if (selectedDeck != null) {
                switchToFlashcardScreen(selectedDeck);
            }
        }
    }

    private void displayFlashcards(Deck deck) {
        if (flashcardScreenController != null) {
            Collection<Flashcard> flashcards = deck.getFlashcards();

            /* checking if flashcards is not null before
            invoking displayFlashcards to avoid the NullPointerException. */
            if (flashcards != null) {
                flashcardScreenController.displayFlashcards(deck);
            }
        }
    }

    public void showDeckScreen() {
        try {
            // Retrieve the stage of the current controller
            Stage currentStage = (Stage) deckListView.getScene().getWindow();

            // Load the FXML file for the deck screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
            Parent root = loader.load();
            Controller deckScreenController = loader.getController();

            // Set any necessary data or configurations on the deck screen controller
            // ...

            // Create a new stage for the deck screen
            Stage deckScreenStage = new Stage();
            deckScreenStage.setTitle("Deck Screen");
            deckScreenStage.setScene(new Scene(root));

            // Hide the current stage and show the deck screen stage
            currentStage.hide();
            deckScreenStage.show();
        } catch (IOException e) {
            // Handle the exception appropriately (e.g., show an error message)
            e.printStackTrace();
        }
    }

    public void initializeDeckScreen() {
        // Load and display the decks
        loadDecks();

        // Set the double click event handler for the deck list view
        deckListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();
                if (selectedDeck != null) {
                    switchToFlashcardScreen(selectedDeck);
                }
            }
        });
    }

    public void closeDeckScreen() {

        if (flashcardScreenController != null) {
            // Show the flashcard screen
            flashcardScreenController.showFlashcardScreen();
        }


        // Reload decks when DeckScreen is closed
        if (flashcardScreenController != null) {
            flashcardScreenController.loadFlashcardsFromStorage();
        }
    }

    /* DELETE BUTTON */
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Deck selectedDeck = deckListView.getSelectionModel().getSelectedItem();
        if (selectedDeck != null) {
            deckList.remove(selectedDeck);
            // Save the updated deck list
            saveDecks();
        }
    }



}
