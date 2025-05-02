package edu.farmingdale.csc311_finalproject;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SpareCreateAccountPageController {
    @FXML
    private TextField FirstNameTextField;

    @FXML
    private Button createAccountButton;

    @FXML
    private PasswordField createAccountConfirmPasswordTextField;

    @FXML
    private TextField createAccountEmailTextField;

    @FXML
    private PasswordField createAccountPasswordTextFIeld;

    @FXML
    private Text emailAddressText;

    @FXML
    private Text firstNameText;

    @FXML
    private Text lastNameText;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private Text passwordText;

    @FXML
    private Text reconfirmPasswordText;

    @FXML
    private ImageView signUpBoard;

    @FXML
    private Text userNameText;

    @FXML
    private TextField userNameTextField;

    public void initialize() {
        signUpBoard.setScaleX(4.1f);
        signUpBoard.setScaleY(5f);
        signUpBoard.setX(-13);
        signUpBoard.setY(-195);
        fadeInNode(FirstNameTextField);
        fadeInNode(createAccountButton);
        fadeInNode(createAccountConfirmPasswordTextField);
        fadeInNode(createAccountEmailTextField);
        fadeInNode(createAccountPasswordTextFIeld);
        fadeInNode(emailAddressText);
        fadeInNode(firstNameText);
        fadeInNode(lastNameText);
        fadeInNode(lastNameTextField);
        fadeInNode(passwordText);
        fadeInNode(reconfirmPasswordText);
        fadeInNode(userNameText);
        fadeInNode(userNameTextField);
    }
    private void fadeInNode(javafx.scene.Node node) {
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.seconds(1), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
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
            showAlert("Passwords do not match", "Please make sure both password fields are identical.");
            return;
        }

        try {
            String email = createAccountEmailTextField.getText();
            String password = createAccountPasswordTextFIeld.getText();
            String username = email.split("@")[0];

            String jsonInput = String.format(
                    "{\"username\":\"%s\",\"fName\":\"First\",\"lName\":\"Last\",\"email\":\"%s\",\"profilePicUrl\":\"\",\"userPassword\":\"%s\"}",
                    username, email, password);

            URL url = new URL("http://localhost:8080/users/create");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();

            if (responseCode == 200 || responseCode == 201) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Account Created");
                alert.setContentText("You may now log in with your new account.");
                alert.showAndWait();

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("spareLoginPage.fxml"));
                Scene loginScene = new Scene(fxmlLoader.load(), 650, 600);
                Stage loginStage = new Stage();
                loginStage.setScene(loginScene);
                loginStage.setTitle("Login Page");
                loginStage.show();

                Stage currentStage = (Stage) createAccountButton.getScene().getWindow();
                currentStage.close();
            } else {
                showAlert("Account Creation Failed", "Server responded with code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Something went wrong during account creation.");
        }
    }

    private void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
