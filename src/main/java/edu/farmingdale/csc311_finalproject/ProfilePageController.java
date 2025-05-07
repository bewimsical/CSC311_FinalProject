package edu.farmingdale.csc311_finalproject;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
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
    private Label friendCount;
    @FXML
    private Label friendsBtn;
    @FXML
    private VBox friendsContainer;
    @FXML
    private FlowPane friendsFlow;
    @FXML
    private Label friendsLabel;
    @FXML
    private ScrollPane friendsScroll;
    @FXML
    private Label gameCount;
    @FXML
    private Label gamesBtn;
    @FXML
    private VBox gamesContainer;
    @FXML
    private FlowPane gamesFlow;
    @FXML
    private ScrollPane gamesScroll;
    @FXML
    private Label homeBtn;
    @FXML
    private MenuItem logout;
    @FXML
    private Label partiesBtn;
    @FXML
    private Label partiesLabel;
    @FXML
    private Label partyCount;
    @FXML
    private StackPane profileContainer;
    @FXML
    private Circle profileImagePlaceholder;
    @FXML
    private MenuButton usernameLabel;
    @FXML
    private StackPane mainProfileContainer;
    @FXML
    private Label mainUsername;

    private User currentUser;
    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private final ObservableList<Game> ownedGames = FXCollections.observableArrayList();
    private final List<Party> parties = new ArrayList<>();

    public ProfilePageController(){
        this.currentUser = Session.getInstance().getUser();
    }

    public ProfilePageController(User user){
        this.currentUser = user;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavBarHandler.setupNav(homeBtn, gamesBtn,friendsBtn,partiesBtn, logout);

        Image image = NavBarHandler.setupNavImage();
        ImageView profilePic = new ImageView(image);
        profilePic.setFitWidth(circle_view.getRadius() * 2);
        profilePic.setFitHeight(circle_view.getRadius() * 2);
        profilePic.setClip(new Circle(circle_view.getRadius(), circle_view.getRadius(), circle_view.getRadius()));

        // Add to StackPane (on top of the Circle)
        profileContainer.getChildren().add(profilePic);
        // Set username
        usernameLabel.setText(Session.getInstance().getUser().getUsername());

        String imgUrl = currentUser.getProfilePicUrl();
        Image profileImg;

        if( checkImage(imgUrl)) {
            try {
                profileImg = new Image(Objects.requireNonNull(NavBarHandler.class.getResource(imgUrl)).toExternalForm());
            } catch (Exception e) {
                profileImg = new Image(Objects.requireNonNull(NavBarHandler.class.getResource("images/wizard_cat.PNG")).toExternalForm());
            }
        }else{
            try {
                profileImg = new Image(imgUrl, true);
            } catch (Exception e) {
                profileImg = new Image(Objects.requireNonNull(NavBarHandler.class.getResource("images/wizard_cat.PNG")).toExternalForm());
            }
        }
        ImageView mainProfilePic = new ImageView(profileImg);
        mainProfilePic.setFitWidth(profileImagePlaceholder.getRadius() * 2);
        mainProfilePic.setFitHeight(profileImagePlaceholder.getRadius() * 2);
        mainProfilePic.setClip(new Circle(profileImagePlaceholder.getRadius(), profileImagePlaceholder.getRadius(), profileImagePlaceholder.getRadius()));

        // Add to StackPane (on top of the Circle)
        mainProfileContainer.getChildren().add(mainProfilePic);
        mainUsername.setText(currentUser.getUsername());

        // Load friends
        try {
            friends.addAll(Objects.requireNonNull(sendGET(getUserFriends(currentUser.getUserId()), new TypeReference<List<User>>() {
            })));
            if (friends.isEmpty()){
                HBox empty = createEmptyCard(" has no friends!");
                friendsFlow.getChildren().add(empty);

            }else {
                for (User friend : friends) {
                    VBox friendCard = createUserCard(friend);
                    friendsFlow.getChildren().add(friendCard);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load games
        try {
            ownedGames.addAll(Objects.requireNonNull(sendGET(getUserGames(currentUser.getUserId()), new TypeReference<List<Game>>() {
            })));
            if(ownedGames.isEmpty()){
                HBox empty = createEmptyCard(" has an empty game cabinet!");
                gamesFlow.getChildren().add(empty);
            }
            for (Game g : ownedGames) {
                HBox gameCard = createGameCard(g);
                gamesFlow.getChildren().add(gameCard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            parties.addAll(Objects.requireNonNull(sendGET(getUserParties(currentUser.getUserId()), new TypeReference<List<Party>>() {
            })));
            for (Game g : ownedGames) {
                HBox gameCard = createGameCard(g);
                gamesFlow.getChildren().add(gameCard);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        gameCount.setText(String.valueOf(ownedGames.size()));
        friendCount.setText(String.valueOf(friends.size()));
        partyCount.setText(String.valueOf(parties.size()));

    }

    public boolean checkImage(String imgUrl){
        String pattern = "^images";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(imgUrl);
        return m.lookingAt();
    }

    private VBox createUserCard(User f) {
        String friendImg = f.getProfilePicUrl();
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
        double friendPicSize = 98;
        friendPic.setFitHeight(friendPicSize);
        friendPic.setFitWidth(friendPicSize);
        friendPic.setPreserveRatio(true);
        friendPic.setSmooth(true);

        StackPane imageContainer = new StackPane(friendPic);
        imageContainer.setPrefSize(friendPicSize, friendPicSize);
        imageContainer.setMaxSize(friendPicSize, friendPicSize);

        Label friendName = new Label(f.getUsername());
        friendName.setWrapText(true);
        friendName.setMaxWidth(98); // adjust based on design
        friendName.setTextAlignment(TextAlignment.CENTER);
        friendName.setAlignment(Pos.CENTER);
        friendName.getStyleClass().add("profile-friend-name-text");

        VBox card = new VBox(10, imageContainer, friendName);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("friend-card");
        double cardSize = 118;
        card.setMaxWidth(cardSize);
        card.setMinWidth(cardSize);
        card.setPrefWidth(cardSize);
        card.setUserData(f.getUserId());
        friendName.prefWidthProperty().bind(card.widthProperty());

        card.setUserData(f.getUserId());
        card.setOnMouseClicked(event -> {

            Long id = (Long)card.getUserData();
            try {
                User user = sendGET(getUserUrl(id), new TypeReference<User>() {});
                try {
                    FXMLLoader loader = new FXMLLoader(AllPartiesController.class.getResource("ProfilePage.fxml"));
                    loader.setControllerFactory(param -> new ProfilePageController(user));
                    Parent root = loader.load();

                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("styles/party-style.css").toExternalForm());
                    Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        FlowPane.setMargin(card, new Insets(10, 5, 0, 5));
        return card;
    }



    private HBox createGameCard(Game g) {
        String gameImg = g.getImgUrl();
        Image gameImage;
        try {
            gameImage = new Image(gameImg, true);
        }catch (Exception e){
            gameImage = new Image(Objects.requireNonNull(getClass().getResource("images/d20_logo_sky.PNG")).toExternalForm());
        }
        ImageView gamePic = new ImageView(gameImage);
        gamePic.setFitHeight(75);
        gamePic.setFitWidth(75);
        gamePic.setPreserveRatio(true);
        gamePic.setSmooth(true);

        StackPane imageContainer = new StackPane(gamePic);
        imageContainer.setPrefSize(75, 75);

        Label gameName = new Label(g.getGame_name());
        gameName.setWrapText(true);
        gameName.setMaxWidth(300);
        gameName.setTextAlignment(TextAlignment.LEFT);
        gameName.setAlignment(Pos.CENTER_LEFT);
        gameName.getStyleClass().add("game-name-text");

        Label gamePlayers = new Label(g.getMinPlayers() + "-" + g.getMaxPlayers() + " players");
        Label gameTime = new Label(g.getPlayTime() + " min");
        gamePlayers.getStyleClass().add("info-text");
        gameTime.getStyleClass().add("info-text");

        VBox textBox = new VBox(5, gameName, gamePlayers, gameTime);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(10, imageContainer, textBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("card");
        card.setMaxWidth(365);
        card.setMinWidth(365);
        FlowPane.setMargin(card, new Insets(10, 5, 0, 5));
        return card;
    }

    private HBox createEmptyCard(String message){
        Label label = new Label(currentUser.getUsername() + message);
        label.getStyleClass().add("selected-party-time-text");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        HBox card = new HBox(10,label);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(365);
        card.setMinWidth(365);
        card.getStyleClass().add("card");
        card.getStyleClass().add("selected-party-card");
        label.maxWidthProperty().bind(card.widthProperty());
        VBox.setMargin(card, new Insets(10, 0, 0, 0));
        return card;
    }


}




