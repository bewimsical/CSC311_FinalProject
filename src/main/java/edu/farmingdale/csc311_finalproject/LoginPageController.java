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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPageController {

    @FXML
    private Text createAccountText;

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

            System.out.println("Sign In successful");


            try {

                FXMLLoader fxmlProfileLoader = new FXMLLoader(HelloApplication.class.getResource("ProfilePage.fxml"));

                Scene profileScene = new Scene(fxmlProfileLoader.load(), 650, 600);

                Stage profileStage = new Stage();
                profileStage.setTitle("Profile Page");
                profileStage.setScene(profileScene);
                profileStage.show();


                Stage currentStage = (Stage) signInButton.getScene().getWindow();
                currentStage.close();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load Profile Page");
                alert.setContentText("An error occurred while trying to load the profile page.");
                alert.showAndWait();
            }}}
    @FXML
    void createAccountHandler(MouseEvent event) {
        try {

            FXMLLoader fxmlCreateAccountLoader = new FXMLLoader(HelloApplication.class.getResource("CreateAccountPage.fxml"));

            Scene createAccountScene = new Scene(fxmlCreateAccountLoader.load(), 600, 400);

            Stage createAccountStage = new Stage();
            createAccountStage.setTitle("Create Account Page");
            createAccountStage.setScene(createAccountScene);
            createAccountStage.show();


            Stage currentStage = (Stage) createAccountText.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load Profile Page");
            alert.setContentText("An error occurred while trying to load the profile page.");
            alert.showAndWait();

        }}

}







