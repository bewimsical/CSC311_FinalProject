package edu.farmingdale.csc311_finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountPageController {

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField createAccountConfirmPasswordTextField;

    @FXML
    private TextField createAccountEmailTextField;

    @FXML
    private TextField createAccountPasswordTextFIeld;

    public void initialize() {
    createAccountButton.setOpacity(.3);
    createAccountButton.setDisable(true);
        createAccountEmailTextField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        createAccountPasswordTextFIeld.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        createAccountConfirmPasswordTextField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        }
        private void checkFields() {
            boolean fieldsFilled = !createAccountEmailTextField.getText().isEmpty() &&
                    !createAccountPasswordTextFIeld.getText().isEmpty() &&
                    !createAccountConfirmPasswordTextField.getText().isEmpty();

            // If all fields are filled, enable the button and make it fully opaque
            if (fieldsFilled) {
                createAccountButton.setOpacity(1); // Set opacity to 1 (fully visible)
                createAccountButton.setDisable(false); // Enable the button
            } else {
                createAccountButton.setOpacity(0.3); // Keep opacity at 0.3 when disabled
                createAccountButton.setDisable(true); // Keep the button disabled
            }
   /** if(!createAccountEmailTextField.getText().isEmpty() && !createAccountPasswordTextFIeld.getText().isEmpty() && !createAccountConfirmPasswordTextField.getText().isEmpty()) {
        createAccountButton.setOpacity(1);
        createAccountButton.setDisable(false);
    }*/

    }

    @FXML
    void createAccountHandler(ActionEvent event) {
        if(!createAccountConfirmPasswordTextField.getText().equals(createAccountPasswordTextFIeld.getText())) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Passwords do not match");
            alert.setContentText("Your passwords do not match");
            alert.showAndWait();
        }
        else{
            try {

                FXMLLoader fxmlProfileLoader = new FXMLLoader(HelloApplication.class.getResource("ProfilePage.fxml"));

                Scene profileScene = new Scene(fxmlProfileLoader.load(), 650, 600);

                Stage profileStage = new Stage();
                profileStage.setTitle("Profile Page");
                profileStage.setScene(profileScene);
                profileStage.show();


                Stage currentStage = (Stage) createAccountButton.getScene().getWindow();
                currentStage.close();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load Profile Page");
                alert.setContentText("An error occurred while trying to load the profile page.");
                alert.showAndWait();
            }
        }

    }


}
