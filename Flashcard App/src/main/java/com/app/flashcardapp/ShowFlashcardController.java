package com.app.flashcardapp;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShowFlashcardController {
    private SingleFlashcardController singleFlashcardController;
    @FXML
    private Button answerButton;
    @FXML
    private Button editButton;
    @FXML
    private Label questionLabel;

    @FXML
    private Label answerLabel;


    @FXML
    private TextField questionTextField;
    @FXML
    private TextArea answerTextArea;

    private Flashcard flashcard;
    private List<Flashcard> flashcards = new ArrayList<>();
    private ObservableList<Flashcard> flashcardList;
    private Stage stage;
    private boolean editMode = false;

    private FlashcardScreenController flashcardScreenController;

    public void setFlashcardScreenController(FlashcardScreenController flashcardScreenController) {
        this.flashcardScreenController = flashcardScreenController;
    }
    public void setFlashcardList(ObservableList<Flashcard> flashcardList) {
        this.flashcardList = flashcardList;
    }

    @FXML
    private void initialize() {
        questionLabel.setVisible(true);
        questionTextField.setVisible(false);
        answerLabel.setVisible(true);
        answerTextArea.setVisible(false);

        editButton.setOnAction(this::onEditButtonClicked);


        answerButton.setOnAction(e -> showAnswer());
    }

    public void enableEditButton() {
        editButton.setDisable(false);
    }





    public void setSingleFlashcardController(SingleFlashcardController singleFlashcardController) {
        this.singleFlashcardController = singleFlashcardController;
    }


    private void saveFlashcard(String question, String answer) {
        Flashcard flashcard = new Flashcard(question, answer);
        flashcards.add(flashcard);
    }



    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    private void clearFields() {
        questionTextField.clear();
        answerTextArea.clear();
    }

    public void setFlashcard(Flashcard flashcard) {
        this.flashcard = flashcard;
        questionLabel.setText(flashcard.getQuestion());
        answerLabel.setText(flashcard.getAnswer());

        // Hide the answer elements
        answerLabel.setVisible(false);
        answerTextArea.setVisible(false);
        answerButton.setDisable(false);
        editButton.setDisable(false);
    }

    @FXML
    private void showAnswer() {
        questionLabel.setVisible(false);
        questionTextField.setVisible(false);
        answerTextArea.setVisible(true);
        answerLabel.setVisible(true);
        answerButton.setDisable(true);
        answerTextArea.setEditable(false);
        editButton.setDisable(false);
    }

    private Consumer<Flashcard> editCallback;

    // Other class members and methods

    public void setEditCallback(Consumer<Flashcard> callback) {
        this.editCallback = callback;
    }

    // Method to handle the edit button action

    @FXML
    private void handleEditButtonAction(ActionEvent event) {
        // Toggle visibility of question elements
        questionLabel.setVisible(!questionLabel.isVisible());
        questionTextField.setVisible(!questionTextField.isVisible());
        questionTextField.setText(questionLabel.getText());

        // Toggle visibility of answer elements
        answerLabel.setVisible(!answerLabel.isVisible());
        answerTextArea.setVisible(!answerTextArea.isVisible());
        answerTextArea.setText(answerLabel.getText());

        if (editCallback != null) {
            editCallback.accept(flashcard);
        }
        openSingleFlashcardEditView();
    }

    private void openSingleFlashcardEditView() {
        if (singleFlashcardController != null) {
            singleFlashcardController.setFlashcard(flashcard);
            singleFlashcardController.openSingleFlashcardView();
        }
    }

    @FXML
    private void onEditButtonClicked(ActionEvent event) {
        // Open the SingleFlashcardController again
        FXMLLoader loader = new FXMLLoader(getClass().getResource("single_flashcard.fxml"));
        Parent root;
        try {
            root = loader.load();
            SingleFlashcardController singleFlashcardController = loader.getController();
            singleFlashcardController.initializeFlashcard(flashcard, true); // Pass a flag indicating edit mode

            Stage stage = new Stage();
            stage.setTitle("Flashcard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current SingleFlashcardController
            Stage currentStage = (Stage) questionLabel.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }


    @FXML
    private void handleSaveButton() {
        if (editMode) {
            String question = questionTextField.getText();
            String answer = answerTextArea.getText();

            // Save the data or perform any further processing
            saveFlashcard(question, answer);

            // Find the index of the current flashcard in the flashcardList
            int index = flashcardList.indexOf(flashcard);

            if (index != -1) {
                // Modify the flashcard at the found index with the updated question and answer
                flashcardList.get(index).setQuestion(question);
                flashcardList.get(index).setAnswer(answer);
            }

            // Clearing input fields
            clearFields();
        }
    }
}
