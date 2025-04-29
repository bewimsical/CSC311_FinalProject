package edu.farmingdale.csc311_finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

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
    private Label usernameLabel; // or Text if you used that!

    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = Session.getInstance().getUser();
        if (user != null) {
            usernameLabel.setText(user.getUsername());
        }
    }


}
