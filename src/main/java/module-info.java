module edu.farmingdale.csc311_finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens edu.farmingdale.csc311_finalproject to javafx.fxml;
    exports edu.farmingdale.csc311_finalproject;
}