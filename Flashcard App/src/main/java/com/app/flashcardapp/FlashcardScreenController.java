package com.app.flashcardapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class FlashcardScreenController implements Initializable {
    @FXML
    private Button deleteButton;
    private Controller deckScreenController;

    private ShowFlashcardController showFlashcardController;
    private SingleFlashcardController singleFlashcardController;

    @FXML
    private ListView<Flashcard> flashcardListView;
    @FXML
    private ObservableList<Flashcard> flashcardList = FXCollections.observableArrayList();



    @FXML
    private Label titleLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private Label answerLabel;

    private Deck deck;

    @FXML
    private Button backButton;
    private Flashcard flashcard;

    private static FlashcardScreenController instance;

    public FlashcardScreenController() {
        instance = this;
    }

    public static FlashcardScreenController getInstance() {
        return instance;
    }

    /* ------------------------- GETTERS SETTERS ---------------------- */

    public void setDeckScreenController(Controller deckScreenController) {
        this.deckScreenController = deckScreenController;
    }

    public void setSingleFlashcardController(SingleFlashcardController singleFlashcardController) {
        this.singleFlashcardController = singleFlashcardController;
    }

    public void setShowFlashcardController(ShowFlashcardController showFlashcardController) {
        this.showFlashcardController = showFlashcardController;
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
        questionLabel.setText(flashcard.getQuestion());
        answerLabel.setText(flashcard.getAnswer());
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        titleLabel.setText(deck.getName());


        // Clear the flashcardList before adding elements
        flashcardList.clear();

        // Populate the flashcards list from the deck
        if (deck != null) {
            Collection<Flashcard> flashcards = deck.getFlashcards();
            if (flashcards != null) {
                flashcardList.addAll(flashcards);
            }
        }
        flashcardListView.setCellFactory(listView -> new FlashcardCell());
    }

    /* ------------------------  INITIALIZE  ---------------------------- */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {


        loadFlashcardsFromStorage(); // Load flashcards when the controller is initialized

        flashcardListView.setCellFactory(listView -> new FlashcardCell());
        flashcardListView.setItems(flashcardList);


        flashcardListView.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) flashcardListView.getScene().getWindow();
                if (stage != null) {
                    // Set the close request handler
                    stage.setOnCloseRequest(event -> closeFlashcardScreen());
                }
            }
        });

        flashcardListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Flashcard selectedFlashcard = flashcardListView.getSelectionModel().getSelectedItem();
                if (selectedFlashcard != null) {
                    instance.openSingleFlashcardView(selectedFlashcard);
                }
            }
        });

        // Initialize titleLabel here
        titleLabel.setText("Flasho");

        if (showFlashcardController != null) {
            showFlashcardController.setFlashcardList(flashcardList);
            // Set the flashcardList in the ShowFlashcardController
        }

        setShowFlashcardController(showFlashcardController); // Pass the ShowFlashcardController instance to FlashcardScreenController

        backButton.setOnAction(this::handleBackButtonAction);
    }




    /* -------------------------- LOADING & SAVING FLASHCARDS --------------------------------- */
    private void saveFlashcardsToStorage(List<Flashcard> flashcards) {
        try {
            File file = new File("flashcards.json");
            saveFlashcardsToFile(flashcards, file);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

    private void saveFlashcardsToFile(List<Flashcard> flashcards, File file) throws IOException {
        if(flashcards!=null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(file, flashcards.toArray(new Flashcard[0]));

        }
    }

    public List<Flashcard> loadFlashcardsFromStorage() {
        List<Flashcard> flashcards = new ArrayList<>();

        try {
            File file = new File("flashcards.json");
            if (file.exists()) {
                flashcards = loadFlashcardsFromFile(file);
                if(flashcardList!=null) {
                    flashcardList.addAll(flashcards);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }

        return flashcards;
    }

    private List<Flashcard> loadFlashcardsFromFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Flashcard[] flashcards = objectMapper.readValue(file, Flashcard[].class);
        return Arrays.asList(flashcards);
    }


    public void displayFlashcards(Deck deck) {
        if (flashcardList != null) {
            flashcardList.clear();

            if (deck != null) {
                Collection<Flashcard> flashcards = deck.getFlashcards();
                if (flashcards != null) {
                    flashcardList.addAll(flashcards);
                }
            }
        }
    }


    /* ---------------------------- HANDLING SWITCHING TO SINGLE FLASHCARD --------------------------- */
    public void openSingleFlashcardView(Flashcard flashcard) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("showFlashcard.fxml"));
            Parent root = loader.load();

            ShowFlashcardController showFlashcardController = loader.getController();
            showFlashcardController.setFlashcard(flashcard);

            Stage stage = new Stage();
            stage.setTitle("Flashcard");
            stage.setScene(new Scene(root));

            // Pass the FlashcardScreenController instance to the ShowFlashcardController
            showFlashcardController.setFlashcardScreenController(this);

            // After the single flashcard view is closed, update the flashcard if it was modified
            if(singleFlashcardController != null) {
                if (singleFlashcardController.isFlashcardModified()) {
                    Flashcard modifiedFlashcard = singleFlashcardController.getFlashcard();
                    // Update the flashcard in the flashcard list
                    flashcardList.set(flashcardList.indexOf(flashcard), modifiedFlashcard);

                    // Update the flashcard in the ShowFlashcardController
                    setFlashcard(modifiedFlashcard);
                }
            }

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleFlashcardClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Double-click event
            Flashcard selectedFlashcard = flashcardListView.getSelectionModel().getSelectedItem();
            if (selectedFlashcard != null) {
                openSingleFlashcardView(selectedFlashcard);
            }
        }
    }

    private void openSingleFlashcardEditView(Flashcard flashcard) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("single_flashcard.fxml"));
            Parent root = loader.load();

            SingleFlashcardController singleFlashcardController = loader.getController();
            singleFlashcardController.setStage(new Stage());
            singleFlashcardController.setFlashcard(flashcard);

            Stage stage = singleFlashcardController.getStage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // After the single flashcard edit view is closed, update the flashcard if it was modified
            if (singleFlashcardController.isFlashcardModified()) {
                Flashcard modifiedFlashcard = singleFlashcardController.getFlashcard();
                // Update the flashcard in the flashcard list
                flashcardList.set(flashcardList.indexOf(flashcard), modifiedFlashcard);

                // Update the flashcard in the show flashcard controller
                ShowFlashcardController showFlashcardController = loader.getController();
                showFlashcardController.setFlashcard(modifiedFlashcard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeFlashcardScreen() {
        // Hide the flashcard screen
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.hide();

        if (deckScreenController != null) {
            // Show the deck screen
            deckScreenController.showDeckScreen();
        }

        saveFlashcardsToStorage(flashcardList);

        //reload decks when FlashcardScreen si closed
        if (deckScreenController != null) {
            deckScreenController.loadDecks();
        }
    }

    public void showFlashcardScreen() {
        try {
            // Save the flashcards to storage
            saveFlashcardsToStorage(flashcardList);

            // Retrieve the stage of the current controller
            Stage currentStage = (Stage) flashcardListView.getScene().getWindow();

            // Load the FXML file for the deck screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("flashcardScreen.fxml"));
            Parent root = loader.load();
            FlashcardScreenController flashcardScreenController = loader.getController();

            // Set any necessary data or configurations on the deck screen controller
            // ...

            // Create a new stage for the deck screen
            Stage flashcardScreenStage = new Stage();
            flashcardScreenStage.setTitle("Flashcard Screen");
            flashcardScreenStage.setScene(new Scene(root));

            // Hide the current stage and show the flashcard screen stage
            currentStage.hide();
            flashcardScreenStage.show();
        } catch (IOException e) {
            // Handle the exception appropriately (e.g., show an error message)
            e.printStackTrace();
        }
    }

    public void initializeFlashcardScreen(Deck deck) {
        if (deck != null) {
            if(flashcardList!=null) {
                flashcardList.clear();
            }
            Collection<Flashcard> flashcards = deck.getFlashcards();
            if (flashcards != null) {
                flashcardList.addAll(flashcards);
            }



            flashcardListView.setItems(flashcardList);

            flashcardListView.setCellFactory(param -> new ListCell<Flashcard>() {
                @Override
                protected void updateItem(Flashcard flashcard, boolean empty) {
                    super.updateItem(flashcard, empty);
                    if (empty || flashcard == null) {
                        setText(null);
                    } else {
                        setText(flashcard.getQuestion());
                    }
                }
            });
        }
    }

    /* -----------------  USING BACK BUTTON TO GO BACK TO DECK --------------------------- */

    public void switchToDeckScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
            Parent root = loader.load();

            Controller deckController = loader.getController();
            deckController.initializeDeckScreen();

            Stage stage = new Stage();
            stage.setTitle("Deck");
            stage.setScene(new Scene(root));
            stage.show();

            // Set the close request handler for the new stage
            stage.setOnCloseRequest(event -> {
                deckController.closeDeckScreen();
                event.consume();
            });

            // Hide the flashcard screen
            Stage flashcardScreenStage = (Stage) backButton.getScene().getWindow();
            flashcardScreenStage.hide();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /* ---------------------------- BUTTON ---------------------------- */

    /* ---------- DELETE ---------- */
    @FXML
    private void handleDeleteButtonAction(ActionEvent event) {
        Flashcard selectedFlashcard = flashcardListView.getSelectionModel().getSelectedItem();
        if (selectedFlashcard != null) {
            flashcardList.remove(selectedFlashcard);
            // Save the updated flashcard list
            File file = new File("flashcards.json");
            saveFlashcardsToStorage(flashcardList);
        }
    }

    /* --------- BACK ---------- */
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        switchToDeckScreen();
    }

    /* ------------- NEW --------- */
    @FXML
    private void handleNewButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("single_flashcard.fxml"));
            Parent root = loader.load();

            SingleFlashcardController singleFlashcardController = loader.getController();
            // Perform any additional initialization or passing data to the singleFlashcardController if needed

            singleFlashcardController.setStage(new Stage());
            singleFlashcardController.setFlashcardList(flashcardList); // Pass the flashcardList to the SingleFlashcardController

            Stage stage = singleFlashcardController.getStage();
            stage.setScene(new Scene(root));
            stage.show();

            // After the flashcard is saved, retrieve the flashcard data from singleFlashcardController
            singleFlashcardController.getStage().setOnCloseRequest(event -> {
                Flashcard flashcard = singleFlashcardController.getFlashcard();
                if (flashcard != null) {
                    flashcardList.add(flashcard);// Add the new flashcard to the list
                    System.out.println("flashcardList.add(flashcard); working***");
                    saveFlashcardsToStorage(flashcardList); // Save the updated flashcard list
                    System.out.println("saveFlashcardsToStorage(flashcardList); working...");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
