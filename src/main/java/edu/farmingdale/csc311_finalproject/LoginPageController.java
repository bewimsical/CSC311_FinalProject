package edu.farmingdale.csc311_finalproject;

import com.google.gson.Gson;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginPageController {
    @FXML
    private Button addPartyBtn;

    @FXML
    private Text createAccountText;

    @FXML
    private Text emailPrompt;


    @FXML
    private ImageView logoImage;

    @FXML
    private Text passwordPrompt;

    @FXML
    private Text forgotPasswordField;

    @FXML
    private Text emailText;

    @FXML
    private Text passwordText;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button signInButton;

    @FXML
    private ImageView loginBoardGame;
    //Ending location should be 69, 78
    //Starting 235,301
    double originalWidth = 148;
    double originalHeight = 73;
    public void initialize() {
        loginBoardGame.setScaleX(4.1f);
        loginBoardGame.setScaleY(5f);
        loginBoardGame.setX(-13);
        loginBoardGame.setY(-195);
        // Set initial opacity to 0 for all elements
        createAccountText.setOpacity(0);
        emailPrompt.setOpacity(0);
        emailTextField.setOpacity(0);
        logoImage.setOpacity(0);
        passwordPrompt.setOpacity(0);
        passwordTextField.setOpacity(0);
        signInButton.setOpacity(0);

        // Create fade transitions for each node (duration: 1 second)
        FadeTransition fade1 = createFade(createAccountText);
        FadeTransition fade2 = createFade(emailPrompt);
        FadeTransition fade3 = createFade(emailTextField);
        FadeTransition fade4 = createFade(logoImage);
        FadeTransition fade5 = createFade(passwordPrompt);
        FadeTransition fade6 = createFade(passwordTextField);
        FadeTransition fade7 = createFade(signInButton);

        // Combine all fades into a parallel transition
        ParallelTransition fadeAll = new ParallelTransition(fade1, fade2, fade3, fade4, fade5, fade6, fade7);

        // Add a 1-second delay before the fade starts
       // PauseTransition delay = new PauseTransition(Duration.seconds(1));

        // Play the delay and then the fades
        SequentialTransition sequence = new SequentialTransition(fadeAll);
        sequence.play();
    }

    private FadeTransition createFade(javafx.scene.Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        return fade;

    }
    @FXML
    private void handleSignIn() {
        if (!emailTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            try {
                String email = emailTextField.getText();
                String password = passwordTextField.getText();

                URL url = new URL(ApiClient.getBaseApiUrl() + "/users/login");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                String jsonInput = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInput.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

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
                    Session.getInstance().setCurrentUserId(loggedInUser.getUserId());

                    //Session.getInstance().setUser(loggedInUser);

//                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfilePage.fxml"));
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-parties-view.fxml"));
                    Scene profileScene = new Scene(fxmlLoader.load(), 850, 560);
                    Stage stage = new Stage();
                    stage.setScene(profileScene);
                    stage.setTitle("");
                    stage.show();

                    Stage currentStage = (Stage) signInButton.getScene().getWindow();
                    currentStage.close();
                } else {
                    showAlert("Login Failed", "Invalid email or password.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Login Error", "An error occurred during login.");
            }
        } else {
            showAlert("Missing Fields", "Please enter both email and password.");
        }
    }

    private void showAlert(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

/**
    @FXML
    void createAccountHandler(MouseEvent event) {
        try {
            FXMLLoader fxmlCreateAccountLoader = new FXMLLoader(HelloApplication.class.getResource("CreateAccountPage.fxml"));
            Parent root = fxmlCreateAccountLoader.load();
            Stage stage = (Stage) createAccountText.getScene().getWindow();
            Scene scene = new Scene(root, 650, 600);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load Profile Page");
            alert.setContentText("An error occurred while trying to load the profile page.");
            alert.showAndWait();
        }
    }
**/
@FXML
void createAccountHandler(MouseEvent event) {
    ParallelTransition fadeAndShake = new ParallelTransition();

    for (javafx.scene.Node node : new javafx.scene.Node[]{
            createAccountText, emailPrompt, emailTextField, passwordPrompt,
            passwordTextField, signInButton, emailText, passwordText, forgotPasswordField, logoImage
    }) {
        if (node != null) {
            fadeAndShake.getChildren().add(createFadeOut(node));
        } else {
            //System.out.println("Warning: One of the nodes is null and will be skipped.");
        }
    }

    // Shake animation for loginBoardGame
    TranslateTransition shake = new TranslateTransition(Duration.millis(100), loginBoardGame); // Match fade duration
    shake.setByX(10);
    shake.setCycleCount(8);
    shake.setAutoReverse(true);
    fadeAndShake.getChildren().add(shake); // Add shake to parallel transition

    fadeAndShake.setOnFinished(e -> {
        try {
            FXMLLoader fxmlCreateAccountLoader = new FXMLLoader(HelloApplication.class.getResource("CreateAccountPage.fxml"));
            Parent root = fxmlCreateAccountLoader.load();
            Stage stage = (Stage) createAccountText.getScene().getWindow();
            Scene scene = new Scene(root, 650, 600);
            scene.getStylesheets().add(
                    getClass().getResource("styles/party-style.css").toExternalForm());
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



    private FadeTransition createFadeOut(javafx.scene.Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1));
        if (node != null) {
            fade.setNode(node);
            fade.setFromValue(1);
            fade.setToValue(0);
        }
        return fade;
    }




    public void setStage(Stage stage) {
        // Handle resize and fullscreen changes
        stage.widthProperty().addListener((obs, oldVal, newVal) -> handleResize(stage));
        stage.heightProperty().addListener((obs, oldVal, newVal) -> handleResize(stage));
        stage.fullScreenProperty().addListener((obs, wasFullScreen, isFullScreen) -> handleResize(stage));
    }

    private void handleResize(Stage stage) {
        double newWidth = stage.getWidth();
        double newHeight = stage.getHeight();

        // Example logic: resize the board image proportionally
        loginBoardGame.setFitWidth(newWidth / 4); // Adjust these ratios to fit your design
        loginBoardGame.setFitHeight(newHeight / 4);

        // You can also reposition or resize other nodes here if needed
    }



    }


