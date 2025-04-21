module edu.farmingdale.csc311_finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens edu.farmingdale.csc311_finalproject to javafx.fxml;
    exports edu.farmingdale.csc311_finalproject;
}