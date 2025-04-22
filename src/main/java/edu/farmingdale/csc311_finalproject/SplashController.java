package edu.farmingdale.csc311_finalproject;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SplashController {

    double originalWidth = 148;
    double originalHeight = 73;
    double enlargedWidth = 180;
    double enlargedHeight = 85;

    @FXML
    private Button byebtn;

    @FXML
    private ImageView signUpBoard;

    @FXML
    private ImageView door;

    @FXML
    private ImageView door1;

    @FXML
    private ImageView logInBoard;

    @FXML
    private Button hellobtn;

    private PauseTransition pause = new PauseTransition(Duration.seconds(5));

    public void initialize() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(2), door);
        transition.setByX(146);
        transition.setCycleCount(1);
        transition.setAutoReverse(true);
        transition.play();

        TranslateTransition transition1 = new TranslateTransition(Duration.seconds(2), door1);
        door1.setX(0);
        transition1.setFromX(0);
        transition1.setFromY(0);
        transition1.setByX(-145);
        transition1.setCycleCount(1);
        transition1.setAutoReverse(true);
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

    @FXML
    void logInPage(MouseEvent event) throws IOException {
        logInBoard.setDisable(true);
        logInBoard.toFront();

        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), logInBoard);
        ScaleTransition st = new ScaleTransition(Duration.seconds(4), logInBoard);

        st.setByX(3.8f);
        st.setByY(7.6f);
        st.setCycleCount(1);
        st.play();

        tt.setToY(-50);
        tt.setCycleCount(1);
        tt.play();

        // Print layout and size after scaling completes
        st.setOnFinished(e -> {
            double layoutX = logInBoard.getLayoutX();
            double layoutY = logInBoard.getLayoutY();
            double translateX = logInBoard.getTranslateX();
            double translateY = logInBoard.getTranslateY();
            double fitWidth = logInBoard.getFitWidth();
            double fitHeight = logInBoard.getFitHeight();

            System.out.println("logInBoard LayoutX: " + layoutX);
            System.out.println("logInBoard LayoutY: " + layoutY);
            System.out.println("logInBoard TranslateX: " + translateX);
            System.out.println("logInBoard TranslateY: " + translateY);
            System.out.println("logInBoard FitWidth: " + fitWidth);
            System.out.println("logInBoard FitHeight: " + fitHeight);
        });

        pause.setOnFinished(e -> {
            try {
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

        pause.play();
    }

    @FXML
    void signUpPage(MouseEvent event) {
        ScaleTransition st1 = new ScaleTransition(Duration.seconds(4), signUpBoard);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), signUpBoard);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));

        signUpBoard.setDisable(true);
        signUpBoard.toFront();

        st1.setByX(3.5f);
        st1.setByY(8.0f);
        st1.setCycleCount(1);
        st1.play();

        tt.setToY(100);
        tt.setCycleCount(1);
        tt.play();

        pause.setOnFinished(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SpareCreateAccountPage.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) signUpBoard.getScene().getWindow();
                Scene scene = new Scene(root, 650, 600);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        pause.play();
    }
}
