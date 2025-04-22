module edu.farmingdale.csc311_finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens edu.farmingdale.csc311_finalproject to javafx.fxml, com.google.gson;

    exports edu.farmingdale.csc311_finalproject;
}