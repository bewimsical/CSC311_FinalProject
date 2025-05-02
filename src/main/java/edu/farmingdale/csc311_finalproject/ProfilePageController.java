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

    private final ObservableList<User> friends = FXCollections.observableArrayList();
    private final ObservableList<Game> ownedGames = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Match PartyController resizing logic
        gamesOwnedLabel.maxWidthProperty().bind(gamesOwnedContainer.widthProperty());
        gamesLabel.maxWidthProperty().bind(gamesList.widthProperty());
        friendsLabel.maxWidthProperty().bind(friendList.widthProperty());

        gamesOwnedListContainer.setFitToWidth(true);
        gamesOwnedList.prefWidthProperty().bind(gamesOwnedListContainer.widthProperty());

        // Load current user
        User user = Session.getInstance().getUser();
        if (user != null) {
            usernameLabel.setText(user.getUsername());

            try {
                File imageFile = new File(new URI(user.getProfilePicUrl()));
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString(), true);
                    ImagePattern pattern = new ImagePattern(image);
                    circle_view.setFill(pattern);
                    circle_view2.setFill(pattern);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Load friends
            try {
                friends.addAll((User) sendGET(getUserFriends(user.getUserId()), new TypeReference<>() {}));
                for (User friend : friends) {
                    HBox friendCard = createUserCard(friend);
                    friendList.getChildren().add(friendCard);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Load games
            try {
                ownedGames.addAll((Game) sendGET(getUserGames(user.getUserId()), new TypeReference<>() {}));
                for (Game g : ownedGames) {
                    HBox gameCard = createGameCard(g);
                    gamesOwnedList.getChildren().add(gameCard);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private HBox createUserCard(User u) {
        String img = u.getProfilePicUrl() != null ? u.getProfilePicUrl() : "images/wizard_cat.PNG";
        Image image = new Image(getClass().getResource(img).toExternalForm());
        ImageView profilePic = new ImageView(image);
        profilePic.setFitHeight(25);
        profilePic.setFitWidth(25);
        profilePic.setPreserveRatio(true);
        profilePic.setSmooth(true);

        Label guestName = new Label(u.getUsername());
        guestName.getStyleClass().add("info-text");

        HBox card = new HBox(10, profilePic, guestName);
        VBox.setMargin(card, new Insets(5, 0, 0, 0));
        return card;
    }

    private HBox createGameCard(Game g) {
        Image gameImage = new Image(g.getImgUrl(), true);
        ImageView gamePic = new ImageView(gameImage);
        gamePic.setFitHeight(75);
        gamePic.setFitWidth(75);
        gamePic.setPreserveRatio(true);
        gamePic.setSmooth(true);

        StackPane imageContainer = new StackPane(gamePic);
        imageContainer.setPrefSize(75, 75);

        Label gameName = new Label(g.getGame_name());
        gameName.setWrapText(true);
        gameName.setMaxWidth(140);
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
        card.setMaxWidth(230);
        card.setMinWidth(230);
        FlowPane.setMargin(card, new Insets(10, 5, 0, 5));
        return card;
    }
}