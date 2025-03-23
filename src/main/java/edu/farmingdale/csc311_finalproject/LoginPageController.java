package edu.farmingdale.csc311_finalproject;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPageController {

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signInButton;

    @FXML
    private Label welcomeText;

    public void initialize() {
        signInButton.setOpacity(.5);

        signInButton.disableProperty().bind(
                Bindings.isEmpty(emailTextField.textProperty())
                        .or(Bindings.isEmpty(passwordTextField.textProperty()))
        );
        signInButton.opacityProperty().bind(
                Bindings.when(Bindings.isEmpty(emailTextField.textProperty()).or(Bindings.isEmpty(passwordTextField.textProperty())))
                        .then(0.3)
                        .otherwise(1.0)
        );

        signInButton.setOnAction(event -> handleSignIn());
    }

    @FXML
    private void handleSignIn() {
        // Check if both fields are filled (non-empty)
        if (!emailTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            // Both fields are not empty, proceed with the action (e.g., sign in logic)
            System.out.println("Sign In successful");

            // Switch to the Profile Page (New Scene)
            try {
                // Load the Profile Page FXML
                FXMLLoader fxmlProfileLoader = new FXMLLoader(HelloApplication.class.getResource("ProfilePage.fxml"));


                // Create a new Scene for the profile page
                Scene profileScene = new Scene(fxmlProfileLoader.load(), 650, 600);

                // Create a new Stage (window) for the profile page
                Stage profileStage = new Stage();
                profileStage.setTitle("Profile Page");
                profileStage.setScene(profileScene);
                profileStage.show();

                // Close the current window (login window)
                Stage currentStage = (Stage) signInButton.getScene().getWindow();
                currentStage.close(); // Close the login window

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load Profile Page");
                alert.setContentText("An error occurred while trying to load the profile page.");
                alert.showAndWait();
            }}}}







