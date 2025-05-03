package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;

public class FriendsController implements Initializable {
    @FXML
    private Circle circle_view;
    @FXML
    private Label friendsBtn;
    @FXML
    private Label gamesBtn;
    @FXML
    private VBox friendsContainer;
    @FXML
    private Label friendsLabel;
    @FXML
    private FlowPane friendsList;
    @FXML
    private ScrollPane friendsListContainer;
    @FXML
    private Label homeBtn;
    @FXML
    private Label partiesBtn;
    @FXML
    private StackPane profileContainer;
    @FXML
    private TextField searchField;
    @FXML
    private MenuButton usernameLabel;

    private User currentUser = Session.getInstance().getUser();
    private final ObservableSet<User> friends =  FXCollections.observableSet();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //navbar handler
        NavBarHandler.setupNav(homeBtn, gamesBtn, friendsBtn, partiesBtn);

//        //todo switch to session user
//        try {
//            currentUser = sendGET(getUserUrl(1), new TypeReference<User>() {
//            });
//
//
//        } catch (IOException e) {
//            currentUser = null;//TODO FIX THIS!!!!!!!
//            e.printStackTrace();
//        }
        String img = currentUser.getProfilePicUrl() != null ? currentUser.getProfilePicUrl() : "images/wizard_cat.PNG";
        Image image;
        // Load and resize image for nav bar
        try {
            image = new Image(Objects.requireNonNull(getClass().getResource(img)).toExternalForm());
        } catch (Exception e) {
            image = new Image(Objects.requireNonNull(getClass().getResource("images/wizard_cat.PNG")).toExternalForm());
        }


        ImageView profilePic = new ImageView(image);
        profilePic.setFitWidth(circle_view.getRadius() * 2);
        profilePic.setFitHeight(circle_view.getRadius() * 2);
        profilePic.setClip(new Circle(circle_view.getRadius(), circle_view.getRadius(), circle_view.getRadius()));

        // Add to StackPane (on top of the Circle)
        profileContainer.getChildren().add(profilePic);

        // Set username
        usernameLabel.setText(currentUser.getUsername());


        try {
            friends.addAll(Objects.requireNonNull(sendGET(getUserFriends(currentUser.getUserId()), new TypeReference<List<User>>() {
            })));
            for (User f : friends) {
                VBox card = createFriendCard(f);
                friendsList.getChildren().add(card);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox createFriendCard(User g){
        String friendImg = g.getProfilePicUrl();
        Image friendImage;
        try {
            friendImage = new Image(Objects.requireNonNull(getClass().getResource(friendImg)).toExternalForm());
        } catch (Exception e) {
            friendImage = new Image(Objects.requireNonNull(getClass().getResource("images/wizard_cat.PNG")).toExternalForm());
        }

        ImageView friendPic = new ImageView(friendImage);
        double friendPicSize = 125;
        friendPic.setFitHeight(friendPicSize);
        friendPic.setFitWidth(friendPicSize);
        friendPic.setPreserveRatio(true);
        friendPic.setSmooth(true);

        StackPane imageContainer = new StackPane(friendPic);
        imageContainer.setPrefSize(friendPicSize, friendPicSize);
        imageContainer.setMaxSize(friendPicSize, friendPicSize);

        Label friendName = new Label(g.getUsername());
        friendName.setWrapText(true);
        friendName.setMaxWidth(166); // adjust based on design
        friendName.setTextAlignment(TextAlignment.CENTER);
        friendName.setAlignment(Pos.CENTER);
        friendName.getStyleClass().add("friend-name-text");


        VBox card = new VBox(10, imageContainer, friendName);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("friend-card");
        double cardSize = 185;
        card.setMaxWidth(cardSize);
        card.setMinWidth(cardSize);
        card.setPrefWidth(cardSize);
        card.setUserData(g.getUserId());
        friendName.prefWidthProperty().bind(card.widthProperty());

        FlowPane.setMargin(card, new Insets(10, 6, 0, 6));
        return card;

    }
}
