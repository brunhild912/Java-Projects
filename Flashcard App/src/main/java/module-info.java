module com.app.flashcardapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.app.flashcardapp to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.app.flashcardapp;
}