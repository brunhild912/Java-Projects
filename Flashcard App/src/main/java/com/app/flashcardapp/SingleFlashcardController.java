package com.app.flashcardapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

public class SingleFlashcardController {
    private boolean editMode = false;
    private boolean flashcardModified;
    @FXML
    private Button editButton;

    @FXML
    private Button saveButton;


    @FXML
    private Label questionLabel;
    @FXML
    private Label answerLabel;

    private List<Flashcard> flashcards = new ArrayList<>();
    private ObservableList<Flashcard> flashcardList;

    @FXML
    private TextField questionTextField;

    @FXML
    private TextArea answerTextArea;

    private Stage stage;
    private Flashcard flashcard;

    private ShowFlashcardController showFlashcardController;
    private FlashcardScreenController flashcardScreenController;


    /* -------------------- GETTERS & SETTERS ---------------------- */

    public void setFlashcardScreenController(FlashcardScreenController flashcardScreenController) {
        this.flashcardScreenController = flashcardScreenController;
    }

    public FlashcardScreenController getFlashcardScreenController() {
        return flashcardScreenController;
    }

    public void setShowFlashcardController(ShowFlashcardController showFlashcardController) {
        this.showFlashcardController = showFlashcardController;
    }

    public void setFlashcardList(ObservableList<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
        // Update the UI components with the flashcard data
        questionTextField.setText(flashcard.getQuestion());
        answerTextArea.setText(flashcard.getAnswer());
    }

    public Flashcard getFlashcard() {
        String question = questionTextField.getText();
        String answer = answerTextArea.getText();

        // Perform any necessary validation or processing on the question and answer

        if (!question.isEmpty() && !answer.isEmpty()) {
            return new Flashcard(question, answer);
        } else {
            // Return null or handle the case when the flashcard is not valid
            return null;
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    /* -------------------------------------------- INITIALIZE ----------------------------------------- */

    @FXML
    private void initialize() {

        // Load the flashcards from storage
        flashcards = loadFlashcardsFromStorage();
        // Initialize the flashcardList
        flashcardList = FXCollections.observableArrayList(flashcards);

        // Set up event handlers
        questionTextField.setOnMouseClicked(this::handleQuestionTextFieldClick);
        answerTextArea.setOnMouseClicked(this::handleAnswerTextAreaClick);

        questionTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                questionTextField.setEditable(false);
            }
        });

        answerTextArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                answerTextArea.setEditable(true);
            }
        });

        if (showFlashcardController!= null) {
            showFlashcardController.setFlashcardList(flashcardList); // Set the flashcardList in the ShowFlashcardController
        }
            setShowFlashcardController(showFlashcardController); // Pass the ShowFlashcardController instance to FlashcardScreenController
    }

    /* ----------------- HANDLING CLICKS ------------------------- */

    @FXML
    private void handleQuestionTextFieldClick(MouseEvent event) {
        // Allow user to start writing when the question text field is clicked
        questionTextField.setEditable(true);
    }

    @FXML
    private void handleAnswerTextAreaClick(MouseEvent event) {
        // Allow user to start writing when the answer text area is clicked
        answerTextArea.setEditable(true);
    }

    @FXML
    private void handleEditButtonClick(ActionEvent event){
        questionTextField.setEditable(true);
        answerTextArea.setEditable(true);

        if (showFlashcardController != null) {
            showFlashcardController.enableEditButton();
        }
    }


    /* ------------------------------------- BUTTONS -------------------------------------- */

    /* ----------- SAVE --------------- */
    @FXML
    private void handleSaveButton() {
        String question = questionTextField.getText();
        String answer = answerTextArea.getText();

        // Create a new flashcard
        Flashcard newFlashcard = new Flashcard(question, answer);

        // Add the new flashcard to the flashcards list
        flashcards.add(newFlashcard);


        // Save the updated flashcardList to storage
        saveFlashcardsToStorage(flashcards);

        // Update the flashcard in the flashcardList if it exists
        if(flashcardList!=null) {
            int index = flashcardList.indexOf(newFlashcard);
            if (index != -1) {
                flashcardList.set(index, newFlashcard);
            }
        }

        // Notify the showFlashcardController of the updated flashcard
        if (showFlashcardController != null) {
            showFlashcardController.setFlashcard(newFlashcard);
        }

        // Close the stage
        closeStage();

        // Clear the input fields
        clearFields();
    }

    /* ------- CLOSE ------ */
    private void handleCloseButton() {
        closeStage();
    }


    /* ------------ INITIALIZE FLASHCARD --------------- */

    public void initializeFlashcard(Flashcard flashcard, boolean editMode) {
        // Set the flashcard data

        // Toggle visibility of elements based on edit mode
        if (editMode) {
            if(questionLabel!=null){
            questionLabel.setVisible(false);}
            if(questionTextField!=null){
            questionTextField.setVisible(true);}
            questionTextField.setText(flashcard.getQuestion());

            if(answerLabel!=null){
            answerLabel.setVisible(false);}
            if(answerTextArea!=null){
            answerTextArea.setVisible(true);}
            answerTextArea.setText(flashcard.getAnswer());

        } else {
            if(questionLabel!=null){
            questionLabel.setVisible(true);}
            if(questionTextField!=null){
            questionTextField.setVisible(false);}
            questionLabel.setText(flashcard.getQuestion());

            if(answerLabel!=null){
            answerLabel.setVisible(true);}
            if(answerTextArea!=null){
            answerTextArea.setVisible(false);}
            answerLabel.setText(flashcard.getAnswer());
        }

        this.editMode = editMode;
    }

    public void openSingleFlashcardView() {
        stage.showAndWait();
        // Save the flashcard to storage when the stage is closed
        saveFlashcardsToStorage(flashcardList);
    }

    /* ---------------- SAVING & LOADING FLASHCARDS METHODS ---------------------- */

    private void saveFlashcardsToStorage(List<Flashcard> flashcards) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File("flashcards.json");

            // Save the flashcards list to the file
            objectMapper.writeValue(file, flashcards);
            System.out.println("Flashcards in SFController saved to a file in storage");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

    private List<Flashcard> loadFlashcardsFromStorage() {
        List<Flashcard> flashcards = new ArrayList<>();

        try {
            File file = new File("flashcards.json");
            if (file.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                flashcards = objectMapper.readValue(file, new TypeReference<List<Flashcard>>() {});
                System.out.println("Flashcards in SFController loaded from storage");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }

        return flashcards;
    }

    /* ------ supporting methods ------ */
    private void clearFields() {
        questionTextField.clear();
        answerTextArea.clear();
    }

    // Method to check if the flashcard was modified
    public boolean isFlashcardModified() {
        return flashcardModified;
    }

    private void closeStage() {
        if(stage!=null) {
            stage.close();
        }
    }


}
