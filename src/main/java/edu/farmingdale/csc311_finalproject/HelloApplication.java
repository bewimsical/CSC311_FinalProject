package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static edu.farmingdale.csc311_finalproject.ApiClient.getUserUrl;
import static edu.farmingdale.csc311_finalproject.ApiClient.sendGET;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //OPENS PARTY PAGE DIRECTLY
        User currentUser = sendGET(getUserUrl(3), new TypeReference<User>() {});
        Session.getInstance().setUser(currentUser);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-parties-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 560);
        //link stylesheet
        scene.getStylesheets().add(getClass().getResource("styles/party-style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}