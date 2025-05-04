package edu.farmingdale.csc311_finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestSplashApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SplashController.class.getResource("TestSplashPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 600);
        scene.getStylesheets().add(getClass().getResource("/edu/farmingdale/csc311_finalproject/styles/party-style.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
