module edu.farmingdale.csc311_finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;


    opens edu.farmingdale.csc311_finalproject to javafx.fxml, com.google.gson;

    exports edu.farmingdale.csc311_finalproject;
}