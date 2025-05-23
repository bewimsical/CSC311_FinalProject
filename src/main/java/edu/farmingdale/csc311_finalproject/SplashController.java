package edu.farmingdale.csc311_finalproject;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SplashController {

    @FXML
    private ImageView insideBox;

    @FXML
    private Text logInText;

    @FXML
    private Text signUpText;

    @FXML
    private ImageView logInBoard;

    @FXML
    private ImageView signUpBoard;
    @FXML
    private ImageView gameNightChest;

    @FXML
    void logInPage(MouseEvent event) {
        logInBoard.setDisable(true);

        // 1. Shake animation for gameNightChest
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), gameNightChest);
        shake.setByX(20);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        TranslateTransition shake1 = new TranslateTransition(Duration.millis(100), insideBox);
        shake1.setByX(20);
        shake1.setCycleCount(6);
        shake1.setAutoReverse(true);
        TranslateTransition shake2 = new TranslateTransition(Duration.millis(100), logInText);
        shake2.setByX(20);
        shake2.setCycleCount(6);
        shake2.setAutoReverse(true);
        TranslateTransition shake3 = new TranslateTransition(Duration.millis(100), signUpText);
        shake3.setByX(20);
        shake3.setCycleCount(6);
        shake3.setAutoReverse(true);

        // 2. After shake, continue with main logInBoard animations
        shake.setOnFinished(event1 -> {
            // Move the board
            TranslateTransition tt = new TranslateTransition(Duration.seconds(2), logInBoard);
            tt.setToY(-195);
            tt.setToX(-13);
            tt.setCycleCount(1);
            tt.play();


            // Bring to front after 2 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> logInBoard.toFront());
            pause.play();

            // After translation is done, do a scale transition
            tt.setOnFinished(e -> {
                ScaleTransition st = new ScaleTransition(Duration.seconds(2), logInBoard);
                st.setToX(4.1);
                st.setToY(5);
                st.setCycleCount(1);
                st.setAutoReverse(false);
                st.play();
                st.setOnFinished(ev2 -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginPage.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) logInBoard.getScene().getWindow();
                        Scene scene = new Scene(root, 650, 600);
                        scene.getStylesheets().add(
                                getClass().getResource("styles/party-style.css").toExternalForm());

                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            });
        });

        // Start the shake
        shake.play();
        shake1.play();
        shake2.play();
        shake3.play();
    }


    @FXML
    void signUpPage(MouseEvent event) {
        signUpBoard.setDisable(true);

        // 1. Shake animation for gameNightChest
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), gameNightChest);
        shake.setByX(20);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        TranslateTransition shake1 = new TranslateTransition(Duration.millis(100), insideBox);
        shake1.setByX(20);
        shake1.setCycleCount(6);
        shake1.setAutoReverse(true);
        TranslateTransition shake2 = new TranslateTransition(Duration.millis(100), logInText);
        shake2.setByX(20);
        shake2.setCycleCount(6);
        shake2.setAutoReverse(true);
        TranslateTransition shake3 = new TranslateTransition(Duration.millis(100), signUpText);
        shake3.setByX(20);
        shake3.setCycleCount(6);
        shake3.setAutoReverse(true);

        // 2. After shake, continue with main logInBoard animations
        shake.setOnFinished(event1 -> {
            // Move the board
            TranslateTransition tt = new TranslateTransition(Duration.seconds(2), signUpBoard);
            tt.setToY(-195);
            tt.setToX(-13);
            tt.setCycleCount(1);
            tt.play();


            // Bring to front after 1.3 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> signUpBoard.toFront());
            pause.play();

            // After translation is done, do a scale transition
            tt.setOnFinished(e -> {
                ScaleTransition st = new ScaleTransition(Duration.seconds(2), signUpBoard);
                st.setToX(4.1);
                st.setToY(5);
                st.setCycleCount(1);
                st.setAutoReverse(false);
                st.play();
                st.setOnFinished(ev2 -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccountPage.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) logInBoard.getScene().getWindow();
                        Scene scene = new Scene(root, 650, 600);
                        scene.getStylesheets().add(
                                getClass().getResource("styles/party-style.css").toExternalForm());
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            });
        });

        // Start the shake
        shake.play();
        shake1.play();
        shake2.play();
        shake3.play();

    }



}



