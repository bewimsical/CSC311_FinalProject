package edu.farmingdale.csc311_finalproject;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class SpareCreateAccountPageController {

    @FXML
    private Text returnToLoginText;

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
            String profilePicUrl = getRandomProfilePicUrl(); // <- get random image URL

            // Debug log
            System.out.println("Profile Picture URL: " + profilePicUrl);

            String jsonInput = String.format(
                    "{\"username\":\"%s\",\"fName\":\"First\",\"lName\":\"Last\",\"email\":\"%s\",\"profilePicUrl\":\"%s\",\"userPassword\":\"%s\"}",
                    username, email, profilePicUrl, password);


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
    private String getRandomProfilePicUrl() {
        try {
            // Adjust this to match your structure in src/main/resources
            File imageFolder = new File(getClass().getResource("/edu/farmingdale/csc311_finalproject/images").toURI());
            File[] imageFiles = imageFolder.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg")
            );

            if (imageFiles != null && imageFiles.length > 0) {
                Random rand = new Random();
                File randomImage = imageFiles[rand.nextInt(imageFiles.length)];
                return randomImage.toURI().toString(); // Returns a URI like file:/.../image.jpg
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log error
        }
        return ""; // Fallback
    }


    private FadeTransition createFadeOut(javafx.scene.Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1));
        if (node != null) {
            fade.setNode(node);
            fade.setFromValue(1);
            fade.setToValue(0);
        }
        return fade;
    }
    private FadeTransition createFade(javafx.scene.Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        return fade;

    }
    @FXML
    void returnToLogin(MouseEvent event) throws IOException {
        ParallelTransition fadeAndShake = new ParallelTransition();

        for (javafx.scene.Node node : new javafx.scene.Node[]{
                returnToLoginText,
                FirstNameTextField,
                createAccountButton,
                createAccountConfirmPasswordTextField,
                createAccountEmailTextField,
                createAccountPasswordTextFIeld,
                emailAddressText,
                firstNameText,
                lastNameText,
                lastNameTextField,
                passwordText,
                reconfirmPasswordText,
                userNameText,
                userNameTextField
        }) {
            if (node != null) {
                fadeAndShake.getChildren().add(createFadeOut(node));
            } else {
                //System.out.println("Warning: One of the nodes is null and will be skipped.");
            }
        }

        // Shake animation for loginBoardGame
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), signUpBoard); // Match fade duration
        shake.setByX(10);
        shake.setCycleCount(8);
        shake.setAutoReverse(true);
        fadeAndShake.getChildren().add(shake); // Add shake to parallel transition

        fadeAndShake.setOnFinished(e -> {
            try {
                FXMLLoader fxmlCreateAccountLoader = new FXMLLoader(HelloApplication.class.getResource("SpareLoginPage.fxml"));
                Parent root = fxmlCreateAccountLoader.load();
                Stage stage = (Stage) returnToLoginText.getScene().getWindow();
                Scene scene = new Scene(root, 650, 600);
                stage.setScene(scene);
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to load Create Account Page");
                alert.setContentText("An error occurred while trying to load the create account page.");
                alert.showAndWait();
            }
        });

        fadeAndShake.play();
    }

    }




