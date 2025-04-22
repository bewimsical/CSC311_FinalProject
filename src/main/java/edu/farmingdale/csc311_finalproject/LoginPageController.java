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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class LoginPageController {

    @FXML
    private Text createAccountText;

    @FXML
    private Text forgotPasswordField;

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
            try {
                String email = emailTextField.getText();
                String password = passwordTextField.getText();

                URL url = new URL("http://localhost:8080/users/login?email=" + email + "&password=" + password);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);

                int responseCode = con.getResponseCode();

                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    Gson gson = new Gson();
                    User loggedInUser = gson.fromJson(response.toString(), User.class);

                    Session.getInstance().setUser(loggedInUser);

                    FXMLLoader fxmlProfileLoader = new FXMLLoader(HelloApplication.class.getResource("ProfilePage.fxml"));
                    Scene profileScene = new Scene(fxmlProfileLoader.load(), 650, 600);
                    Stage profileStage = new Stage();
                    profileStage.setTitle("Profile Page");
                    profileStage.setScene(profileScene);
                    profileStage.show();

                    Stage currentStage = (Stage) signInButton.getScene().getWindow();
                    currentStage.close();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText("Invalid Credentials");
                    alert.setContentText("Please check your email and password.");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Login Failed");
                alert.setContentText("An error occurred during login.");
                alert.showAndWait();
            }
        }
    }


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

    @FXML
    void forgotPasswordHandler(MouseEvent event) {
        try {

            FXMLLoader fxmlForgotPasswordLoader = new FXMLLoader(HelloApplication.class.getResource("ForgotPasswordPage.fxml"));

            Scene forgotPasswordScene = new Scene(fxmlForgotPasswordLoader.load(), 600, 400);

            Stage forgotPasswordStage = new Stage();
            forgotPasswordStage.setTitle("Forgot Password Page");
            forgotPasswordStage.setScene(forgotPasswordScene);
            forgotPasswordStage.show();


            Stage currentStage = (Stage) forgotPasswordField.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load Page");
            alert.setContentText("An error occurred while trying to load the page.");
            alert.showAndWait();

        }}

    }









