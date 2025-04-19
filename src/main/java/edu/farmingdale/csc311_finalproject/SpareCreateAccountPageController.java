package edu.farmingdale.csc311_finalproject;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


public class SpareCreateAccountPageController {


    @FXML
    private Button createAccountButton;

    @FXML
    private TextField createAccountConfirmPasswordTextField;

    @FXML
    private TextField createAccountEmailTextField;

    @FXML
    private TextField createAccountPasswordTextFIeld;

    @FXML
    private Text emailAddressText;

    @FXML
    private Text passwordText;


    @FXML
    private ImageView signUpBoard;

    @FXML
    private Text reconfirmPasswordText;


    public void initialize() {
        signUpBoard.setScaleX(4.1f);
        signUpBoard.setScaleY(5f);
        signUpBoard.setX(-13);
        signUpBoard.setY(-195);
       /**
        signUpBoard.setScaleX(1.5f);
        signUpBoard.setScaleY(1.5f);
        ScaleTransition st = new ScaleTransition(Duration.seconds(4), signUpBoard);
        st.setToX(1);
        st.setToY(1);
        st.setCycleCount(1);

        // After scale transition ends, fade in the other elements
        st.setOnFinished(e -> fadeInElements());

        st.play();
        reconfirmPasswordText.setOpacity(0);
        createAccountConfirmPasswordTextField.setOpacity(0);
        createAccountEmailTextField.setOpacity(0);
        createAccountPasswordTextFIeld.setOpacity(0);
        emailAddressText.setOpacity(0);
        passwordText.setOpacity(0);
    createAccountButton.setOpacity(0);
    createAccountButton.setDisable(true);
        createAccountEmailTextField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        createAccountPasswordTextFIeld.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        createAccountConfirmPasswordTextField.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        }
    private void fadeInElements() {
        // Create fade transitions for each element
        FadeTransition fadeCreateAccount = new FadeTransition(Duration.seconds(2), createAccountButton);
        fadeCreateAccount.setFromValue(0);
        fadeCreateAccount.setToValue(1);

        FadeTransition fadeForgotPassword = new FadeTransition(Duration.seconds(2), createAccountConfirmPasswordTextField);
        fadeForgotPassword.setFromValue(0);
        fadeForgotPassword.setToValue(1);

        FadeTransition fadeEmailTextField = new FadeTransition(Duration.seconds(2), createAccountEmailTextField);
        fadeEmailTextField.setFromValue(0);
        fadeEmailTextField.setToValue(1);

        FadeTransition fadePasswordTextField = new FadeTransition(Duration.seconds(2), createAccountPasswordTextFIeld);
        fadePasswordTextField.setFromValue(0);
        fadePasswordTextField.setToValue(1);

        FadeTransition fadeSignInButton = new FadeTransition(Duration.seconds(2), emailAddressText);
        fadeSignInButton.setFromValue(0);
        fadeSignInButton.setToValue(1);

        FadeTransition fadeinPasswordText = new FadeTransition(Duration.seconds(2), passwordText);
        fadeinPasswordText.setFromValue(0);
        fadeinPasswordText.setToValue(1);

        FadeTransition fadeInConfirmPassword = new FadeTransition(Duration.seconds(2), createAccountConfirmPasswordTextField);
        fadeInConfirmPassword.setFromValue(0);
        fadeInConfirmPassword.setToValue(1);

        // Play all fade transitions in parallel
        fadeCreateAccount.play();
        fadeForgotPassword.play();
        fadeEmailTextField.play();
        fadePasswordTextField.play();
        fadeSignInButton.play();
        fadeinPasswordText.play();
        fadeInConfirmPassword.play();
    }
        **/
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
        if(!createAccountConfirmPasswordTextField.getText().equals(createAccountPasswordTextFIeld.getText())) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Passwords do not match");
            alert.setContentText("Your passwords do not match");
            alert.showAndWait();
        }
        else{
            try {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success!");
                alert.setHeaderText("Account Created");
                alert.setContentText("Your account has been created successfully.");
                alert.showAndWait();


                Stage currentStage = (Stage) createAccountButton.getScene().getWindow();
                currentStage.close();
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("spareLoginPage.fxml"));

                Scene loginScene = new Scene(fxmlLoader.load(), 650, 600);

                Stage loginStage = new Stage();

                loginStage.setScene(loginScene);
                loginStage.show();


                Stage currentAlertStage = (Stage) createAccountButton.getScene().getWindow();
                currentAlertStage.close();
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
