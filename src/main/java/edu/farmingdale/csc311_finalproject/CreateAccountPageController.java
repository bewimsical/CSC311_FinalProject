package edu.farmingdale.csc311_finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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


            if (fieldsFilled) {
                createAccountButton.setOpacity(1);
                createAccountButton.setDisable(false);
            } else {
                createAccountButton.setOpacity(0.3);
                createAccountButton.setDisable(true);
            }


    }

    @FXML
    void createAccountHandler(ActionEvent event) {
        if (!createAccountConfirmPasswordTextField.getText().equals(createAccountPasswordTextFIeld.getText())) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Passwords do not match");
            alert.setContentText("Your passwords do not match");
            alert.showAndWait();
        } else {
            try {
                //creates JSON
                String jsonInputString = String.format(
                        "{\"username\":\"%s\", \"fName\":\"First\", \"lName\":\"Last\", \"email\":\"%s\", \"password\":\"%s\", \"profilePicUrl\":\"\"}",
                        createAccountEmailTextField.getText().split("@")[0], // temp username
                        createAccountEmailTextField.getText(),
                        createAccountPasswordTextFIeld.getText()
                );

                //send POST request
                URL url = new URL("http://localhost:8080/users/create");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = con.getResponseCode();
                if (responseCode == 200 || responseCode == 201) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Success!");
                    alert.setHeaderText("Account Created");
                    alert.setContentText("Your account has been created successfully!");
                    alert.showAndWait();

                    Stage currentStage = (Stage) createAccountButton.getScene().getWindow();
                    currentStage.close();
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginPage.fxml"));
                    Scene loginScene = new Scene(fxmlLoader.load(), 530, 500);
                    Stage loginStage = new Stage();
                    loginStage.setScene(loginScene);
                    loginStage.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Account Creation Failed");
                    alert.setContentText("Server responded with status code: " + responseCode);
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to create account");
                alert.setContentText("Something went wrong!");
                alert.showAndWait();
            }
        }


    }
}
