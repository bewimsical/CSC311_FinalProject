package edu.farmingdale.csc311_finalproject;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class SplashController {
    double originalWidth=148;
    double originalHeight=73;

    double enlargedWidth=180;
    double enlargedHeight=85;

        @FXML
        private Button byebtn;

    @FXML
    private ImageView signUpBoard;

        @FXML
        private ImageView door;

        @FXML
        private ImageView logInBoard;

        @FXML
        private Button hellobtn;

        @FXML
        private ImageView door1;
        public void initialize() {

            // Use a TranslateTransition to animate the door opening over time
            TranslateTransition transition = new TranslateTransition(Duration.seconds(2), door);
            transition.setByX(146);  // Move door to the right by 100 pixels
            transition.setCycleCount(1); // Execute the animation once
            transition.setAutoReverse(true); // Optional: reverse after animation
            transition.play();

            // You can also flip the door after the animation
            //transition.setOnFinished(event -> door.setScaleX(-1));  // Flip the door once the animation ends

            // Use a TranslateTransition to animate the door opening over time
            TranslateTransition transition1 = new TranslateTransition(Duration.seconds(2), door1);
            //transition1.setOnFinished(event -> door1.setScaleX(-1));
            door1.setX(0);
            transition1.setFromX(0);
            transition1.setFromY(0);
            transition1.setByX(-145);  // Move door to the right by 100 pixels
            transition1.setCycleCount(1); // Execute the animation once
            transition1.setAutoReverse(true); // Optional: reverse after animation
            transition1.play();
            logInBoard.setOnMouseEntered(event -> {
                logInBoard.setFitWidth(enlargedWidth);
                logInBoard.setFitHeight(enlargedHeight);
            });

            logInBoard.setOnMouseExited(event -> {
                logInBoard.setFitWidth(originalWidth);
                logInBoard.setFitHeight(originalHeight);
            });
            signUpBoard.setOnMouseEntered(event -> {
                signUpBoard.setFitWidth(enlargedWidth);
                signUpBoard.setFitHeight(enlargedHeight);
            });

            signUpBoard.setOnMouseExited(event -> {
                signUpBoard.setFitWidth(originalWidth);
                signUpBoard.setFitHeight(originalHeight);
            });



        }
        ScaleTransition st = new ScaleTransition(Duration.seconds(4), logInBoard);
        ScaleTransition st1 = new ScaleTransition(Duration.seconds(4), signUpBoard);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), logInBoard);
    PauseTransition pause = new PauseTransition(Duration.seconds(5));
    @FXML
    void logInPage(MouseEvent event) throws IOException {
        logInBoard.setDisable(true);

            logInBoard.toFront(); // Bring this image to the front
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), logInBoard);

        ScaleTransition st = new ScaleTransition(Duration.seconds(4), logInBoard);
 // Adjust time as needed
st.setByX(3.8f);
st.setByY(7.6f);
st.setCycleCount(1);
st.play();
//st1.setByX(1000);
tt.setByY(0);
        tt.setToY(-50);
tt.setCycleCount(1);
tt.play();

        // Define what happens after the delay
        pause.setOnFinished(e -> {
            try {
                // Load the next scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("spareLoginPage.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) logInBoard.getScene().getWindow();
                Scene scene = new Scene(root, 650, 600);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Start the delay
        pause.play();
    }

    @FXML
    void signUpPage(MouseEvent event) {
        ScaleTransition st1 = new ScaleTransition(Duration.seconds(4), signUpBoard);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), signUpBoard);

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        signUpBoard.setDisable(true);

        signUpBoard.toFront(); // Bring this image to the front

        // Adjust time as needed
        st1.setByX(3.5f);
        st1.setByY(8.0f);
        st1.setCycleCount(1);
        st1.play();
//st1.setByX(1000);
        tt.setByY(0);
        tt.setToY(100);
        tt.setCycleCount(1);
        tt.play();

        // Define what happens after the delay
        pause.setOnFinished(e -> {
            try {
                // Load the next scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("createAccountPage.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) signUpBoard.getScene().getWindow();
                Scene scene = new Scene(root, 650, 600);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Start the delay
        pause.play();
    }

    }
        /**
    @FXML
    void increaseSize(MouseEvent event) {
        logInBoard.setFitWidth(enlargedWidth);
        logInBoard.setFitHeight(enlargedHeight);
    }
    @FXML
    void handleMouseMoved(MouseEvent event) {
        // Shrink back when just hovered over
        logInBoard.setFitWidth(originalWidth);
        logInBoard.setFitHeight(originalHeight);
    }**/


