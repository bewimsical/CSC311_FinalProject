package edu.farmingdale.csc311_finalproject;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpareLoginPageController {
    @FXML
    private ImageView loginBoardGame;
    public void initialize() {
        loginBoardGame.setScaleX(3.5f);
        loginBoardGame.setScaleY(8.5f);
    ScaleTransition st = new ScaleTransition(Duration.seconds(4), loginBoardGame);
    // Adjust time as needed
st.setByX(-3.5f);
st.setByY(-8.5f);
st.setCycleCount(1);
st.play();
}}
