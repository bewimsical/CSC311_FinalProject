package edu.farmingdale.csc311_finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfilePageController {

    @FXML
    private Label welcomeText;

    //@FXML
    //private Text usernameLabel;

    @FXML
    private Label emailLabel;



    @FXML
    private Label usernameLabel;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = Session.getInstance().getUser();
        if (user != null) {
            usernameLabel.setText(user.getUsername());
        }
    }


    @FXML
    public void openCreatePartyPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("create-party.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("styles/party-style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Create Party");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goToPartyPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("party-page.fxml"));
            Parent partyPage = loader.load();
            Scene scene = new Scene(partyPage);
            Stage currentStage = (Stage) usernameLabel.getScene().getWindow(); // or any component from the current scene
            currentStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
