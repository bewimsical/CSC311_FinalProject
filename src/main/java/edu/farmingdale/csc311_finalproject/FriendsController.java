package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.farmingdale.csc311_finalproject.UserDto;
import javafx.stage.Popup;
import javafx.stage.Stage;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;

public class FriendsController implements Initializable {
    @FXML
    private Circle circle_view;
    @FXML
    private Label friendsBtn;
    @FXML
    private Label gamesBtn;
    @FXML
    private MenuItem logout;
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
    @FXML
    private TextField searchFriendField;

    private User currentUser = Session.getInstance().getUser();
    private final ObservableSet<User> friends =  FXCollections.observableSet();

    private Popup searchPopup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //navbar handler
        NavBarHandler.setupNav(homeBtn, gamesBtn, friendsBtn, partiesBtn,logout);

        Image image = NavBarHandler.setupNavImage();

        ImageView profilePic = new ImageView(image);
        profilePic.setFitWidth(circle_view.getRadius() * 2);
        profilePic.setFitHeight(circle_view.getRadius() * 2);
        profilePic.setClip(new Circle(circle_view.getRadius(), circle_view.getRadius(), circle_view.getRadius()));

        // Add to StackPane (on top of the Circle)
        profileContainer.getChildren().add(profilePic);

        // Set username
        usernameLabel.setText(currentUser.getUsername());

        searchPopup = new Popup();
        searchPopup.setAutoHide(true);

        try {
            friends.addAll(Objects.requireNonNull(sendGET(getUserFriends(currentUser.getUserId()), new TypeReference<List<User>>() {
            })));
            for (User f : friends) {
                VBox card = createFriendCard(f);
                friendsList.getChildren().add(card);
                //searchResultsVBox.getChildren().add(card);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox createFriendCard(User g){
        String friendImg = g.getProfilePicUrl();
        Image friendImage;
        if( checkImage(friendImg)) {
            try {
                friendImage = new Image(Objects.requireNonNull(getClass().getResource(friendImg)).toExternalForm());
            } catch (Exception e) {
                friendImage = new Image(Objects.requireNonNull(getClass().getResource("images/wizard_cat.PNG")).toExternalForm());
            }
        }else{
            try {
                friendImage = new Image(friendImg, true);
            } catch (Exception e) {
                friendImage = new Image(Objects.requireNonNull(getClass().getResource("images/wizard_cat.PNG")).toExternalForm());
            }
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

    public boolean checkImage(String imgUrl){
        String pattern = "^images";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(imgUrl);
        return m.lookingAt();
    }

    @FXML
    private void handleSearchFriend() {
        String query = searchFriendField.getText().trim();
        if (!query.isEmpty()) {
            searchFriendsByUsername(query);
        }
    }

    private void searchFriendsByUsername(String username) {
        System.out.println("Searching for: " + username);//method confirmation
        List<User> results = new ArrayList<>();
        try {
            results.addAll(Objects.requireNonNull(sendGET(ApiClient.searchFriendsByUsername(username), new TypeReference<List<User>>() {
            })));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!searchPopup.isShowing()) {
            Label resultsLabel = new Label("Search Results");
            resultsLabel.getStyleClass().add("search-results-heading");
            resultsLabel.setMaxWidth(Double.MAX_VALUE);
            FlowPane flow = new FlowPane();
            flow.setHgap(10);
            flow.setVgap(10);
            flow.setPadding(new Insets(10));
            ScrollPane addFriendsListContainer = new ScrollPane(flow);
            VBox searchResultsVBox = new VBox(resultsLabel, addFriendsListContainer);
            searchResultsVBox.setPrefWidth(700);
            searchResultsVBox.setAlignment(Pos.TOP_CENTER);
            resultsLabel.setAlignment(Pos.CENTER);
            resultsLabel.maxWidthProperty().bind(searchResultsVBox.widthProperty());
            resultsLabel.setTextAlignment(TextAlignment.CENTER);
            searchResultsVBox.getStyleClass().add("add-friends-popup");
            addFriendsListContainer.setPrefSize(700, 300);


            for (User user : results) {
                VBox friendCard = createFriendCard(user);
                Button addFriendBtn = new Button("Add");
                addFriendBtn.setOnAction(e -> sendAddFriendRequest(currentUser.getUserId(), user.getUserId()));
                addFriendBtn.getStyleClass().add("friends-popup-button");
                friendCard.getChildren().add(addFriendBtn);

                flow.getChildren().add(friendCard);
            }

            searchPopup.getContent().add(searchResultsVBox);

            Stage stage = (Stage) ((Node) homeBtn.getParent()).getScene().getWindow();
            double x = stage.getX() + (stage.getWidth() - 700)/2 ;
            double y = stage.getY() + 210;
            searchPopup.show(stage, x, y);
        }
    }

    private void sendAddFriendRequest(Long userId, Long friendId) {

        System.out.println(userId + " " + friendId);
        try {
            User friend = sendPOST(addFriendToUser(userId, friendId), null, new TypeReference<User>() {
            });
            if (friend != null) {
                VBox newFriend = createFriendCard(friend);
                friendsList.getChildren().add(newFriend);
                searchPopup.hide();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("friend not added");//TODO add label or warning
        }
    }








}
