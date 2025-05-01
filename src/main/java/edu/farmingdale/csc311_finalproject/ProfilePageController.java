package edu.farmingdale.csc311_finalproject;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;
public class ProfilePageController implements Initializable {

    @FXML
    private Circle circle_view;
    @FXML
    private MenuButton usernameLabel;
    @FXML
    private StackPane profileContainer;
    @FXML
    private Circle circle_view2;
    @FXML
    private Label friendsLabel;
    @FXML
    private VBox friendList;
    @FXML
    private Label gamesLabel;
    @FXML
    private VBox gamesList;
    @FXML
    private VBox gamesOwnedContainer;
    @FXML
    private Label gamesOwnedLabel;
    @FXML
    private ScrollPane gamesOwnedListContainer;
    @FXML
    private FlowPane gamesOwnedList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //keep things centered when app size changes
       /* gamesLabel.maxWidthProperty().bind(gamesContainer.widthProperty());
        selectedGamesLabel.maxWidthProperty().bind(selectedGamesContainer.widthProperty());
        gamesListContainer.setFitToWidth(true);
        gamesList.prefWidthProperty().bind(gamesListContainer.widthProperty());

        */
        User user = Session.getInstance().getUser();
        if (user != null) {
            usernameLabel.setText(user.getUsername());
            //  emailLabel.setText(user.getEmail());
            // welcomeText.setText("Welcome, " + user.getUsername());

            // Load and set the profile picture
            try {
                File imageFile = new File(new URI(user.getProfilePicUrl()));
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString(), true);
                    circle_view.setFill(new ImagePattern(image));
                    circle_view2.setFill(new ImagePattern(image));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

