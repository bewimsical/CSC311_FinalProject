package edu.farmingdale.csc311_finalproject;

import com.google.gson.Gson;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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

public class SpareLoginPageController {
    @FXML
    private Text createAccountText;

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
        /**
        TranslateTransition tt = new TranslateTransition(Duration.millis(1), loginBoardGame);
        tt.setToY(-195);
        tt.setToX(-13);
        tt.setCycleCount(1);
        tt.play();
         /**
       /** loginBoardGame.setScaleX(1.48f);
        loginBoardGame.setScaleY(1.42f);

        // Initially set opacity of all elements to 0
        createAccountText.setOpacity(0);
        forgotPasswordField.setOpacity(0);
        emailTextField.setOpacity(1);
        passwordTextField.setOpacity(0);
        signInButton.setOpacity(0);
        emailText.setOpacity(0);
        passwordText.setOpacity(0);

        // Scale transition for loginBoardGame
        ScaleTransition st = new ScaleTransition(Duration.seconds(4), loginBoardGame);
        st.setToX(1);
        st.setToY(1);
        st.setCycleCount(1);
        TranslateTransition tt = new TranslateTransition(Duration.seconds(2), loginBoardGame);
        tt.setToY(12);
        tt.setCycleCount(1);
        tt.setAutoReverse(false);
        tt.play();


        // After scale transition ends, fade in the other elements
        st.setOnFinished(e -> fadeInElements());

        st.play();
    }

    private void fadeInElements() {
        // Create fade transitions for each element
        FadeTransition fadeCreateAccount = new FadeTransition(Duration.seconds(2), createAccountText);
        fadeCreateAccount.setFromValue(0);
        fadeCreateAccount.setToValue(1);

        FadeTransition fadeForgotPassword = new FadeTransition(Duration.seconds(2), forgotPasswordField);
        fadeForgotPassword.setFromValue(0);
        fadeForgotPassword.setToValue(1);

        FadeTransition fadeEmailTextField = new FadeTransition(Duration.seconds(2), emailTextField);
        fadeEmailTextField.setFromValue(0);
        fadeEmailTextField.setToValue(1);

        FadeTransition fadePasswordTextField = new FadeTransition(Duration.seconds(2), passwordTextField);
        fadePasswordTextField.setFromValue(0);
        fadePasswordTextField.setToValue(1);

        FadeTransition fadeSignInButton = new FadeTransition(Duration.seconds(2), signInButton);
        fadeSignInButton.setFromValue(0);
        fadeSignInButton.setToValue(1);

        FadeTransition fadeEmailText = new FadeTransition(Duration.seconds(2), emailText);
        fadeEmailText.setFromValue(0);
        fadeEmailText.setToValue(1);

        FadeTransition fadePasswordText = new FadeTransition(Duration.seconds(2), passwordText);
        fadePasswordText.setFromValue(0);
        fadePasswordText.setToValue(1);

        // Play all fade transitions in parallel
        fadeCreateAccount.play();
        fadeForgotPassword.play();
        fadeEmailTextField.play();
        fadePasswordTextField.play();
        fadeSignInButton.play();
        fadeEmailText.play();
        fadePasswordText.play();
    }
**/
    }
    @FXML
    private void handleSignIn() {
        if (!emailTextField.getText().isEmpty() && !passwordTextField.getText().isEmpty()) {
            try {
                String email = emailTextField.getText();
                String password = passwordTextField.getText();

                URL url = new URL("http://localhost:8080/users/login");
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

//                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfilePage.fxml"));
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("all-parties-view.fxml"));
                    Scene profileScene = new Scene(fxmlLoader.load(), 850, 560);
                    Stage stage = new Stage();
                    stage.setScene(profileScene);
                    stage.setTitle("Profile Page");
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


    @FXML
    void createAccountHandler(MouseEvent event) {
        try {
            FXMLLoader fxmlCreateAccountLoader = new FXMLLoader(HelloApplication.class.getResource("SpareCreateAccountPage.fxml"));
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
        }
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


