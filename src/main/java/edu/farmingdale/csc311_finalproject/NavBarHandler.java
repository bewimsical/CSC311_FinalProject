package edu.farmingdale.csc311_finalproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class NavBarHandler {

    private NavBarHandler(){}

    public static void setupNav(Label home, Label games, Label friends, Label parties){
        home.setOnMouseClicked(e -> switchScene(e.getSource(), "ProfilePage.fxml"));
        games.setOnMouseClicked(e -> switchScene(e.getSource(), "games-view.fxml"));
        friends.setOnMouseClicked(e -> switchScene(e.getSource(), "friends-view.fxml"));
        parties.setOnMouseClicked(e -> switchScene(e.getSource(), "all-parties-view.fxml"));
    }

    private static void switchScene(Object source, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(NavBarHandler.class.getResource(fxmlFile));
            Stage stage = (Stage)((Node)source).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
