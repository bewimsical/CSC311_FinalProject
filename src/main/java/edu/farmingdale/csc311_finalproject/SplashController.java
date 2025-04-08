package edu.farmingdale.csc311_finalproject;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


public class SplashController {

        @FXML
        private Button byebtn;

        @FXML
        private ImageView door;

        @FXML
        private Button hellobtn;

        @FXML
        private ImageView door1;
        public void initialize() {

            // Use a TranslateTransition to animate the door opening over time
            TranslateTransition transition = new TranslateTransition(Duration.seconds(2), door);
            transition.setByX(135);  // Move door to the right by 100 pixels
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

            // You can also flip the door after the animation
            //transition1.setOnFinished(event -> door1.setScaleX(-1));  // Flip the door once the animation ends


        }
}
