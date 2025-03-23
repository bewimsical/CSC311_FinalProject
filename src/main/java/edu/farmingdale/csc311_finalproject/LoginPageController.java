package edu.farmingdale.csc311_finalproject;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

        if (!emailTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sign In");
            alert.setHeaderText(null);
            alert.setContentText("Both fields are filled, proceeding with sign-in.");
            alert.showAndWait();


        }

    }

        };





