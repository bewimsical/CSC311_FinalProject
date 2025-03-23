package edu.farmingdale.csc311_finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginPage.fxml"));
        //Profile page FXML
        //FXMLLoader fxmlProfileLoader = new FXMLLoader(HelloApplication.class.getResource("ProfilePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 530, 500);
       //Profile page layout
        //Scene scene = new Scene(fxmlLoader.load(), 650, 600);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}