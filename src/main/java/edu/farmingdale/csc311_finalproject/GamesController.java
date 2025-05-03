package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;

public class GamesController implements Initializable {

    @FXML
    private Circle circle_view;
    @FXML
    private Label friendsBtn;
    @FXML
    private Label gamesBtn;
    @FXML
    private VBox gamesContainer;
    @FXML
    private Label gamesLabel;
    @FXML
    private FlowPane gamesList;
    @FXML
    private ScrollPane gamesListContainer;
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

    private User currentUser;
    private final ObservableSet<Game> games =  FXCollections.observableSet();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //navbar handler
        NavBarHandler.setupNav(homeBtn, gamesBtn,friendsBtn,partiesBtn);

        //todo switch to session user
        try {
            currentUser = sendGET(getUserUrl(1), new TypeReference<User>() {});
            String img = currentUser.getProfilePicUrl() != null ? currentUser.getProfilePicUrl() : "images/wizard_cat.PNG";
            Image image;
            // Load and resize image for nav bar
            try {
                image = new Image(Objects.requireNonNull(getClass().getResource(img)).toExternalForm());
            }catch (Exception e){
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



        } catch (IOException e) {
            currentUser = null;//TODO FIX THIS!!!!!!!
            e.printStackTrace();
        }

        try {
            games.addAll(Objects.requireNonNull(sendGET(getUserGames(currentUser.getUserId()), new TypeReference<List<Game>>() {
            })));
            for (Game g : games) {
                HBox card = createGameCard(g);
                gamesList.getChildren().add(card);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public HBox createGameCard(Game g){
        String gameImg = g.getImgUrl();
        Image gameImage = new Image(gameImg, true);
        ImageView gamePic = new ImageView(gameImage);
        double gamePicSize = 125;
        gamePic.setFitHeight(gamePicSize);
        gamePic.setFitWidth(gamePicSize);
        gamePic.setPreserveRatio(true);
        gamePic.setSmooth(true);

        StackPane imageContainer = new StackPane(gamePic);
        imageContainer.setPrefSize(gamePicSize, gamePicSize);
        imageContainer.setMaxSize(gamePicSize, gamePicSize);

        Label gameName = new Label(g.getGame_name());
        gameName.setWrapText(true);
        gameName.setMaxWidth(166); // adjust based on design
        gameName.setTextAlignment(TextAlignment.LEFT);
        gameName.setAlignment(Pos.CENTER_LEFT);
        gameName.getStyleClass().add("games-game-name-text");

        Label gamePlayers = new Label(g.getMinPlayers() + "-" + g.getMaxPlayers() + " players");
        Label gameTime = new Label(g.getPlayTime() + " min");
        gamePlayers.getStyleClass().add("games-info-text");
        gameTime.getStyleClass().add("games-info-text");

        VBox textBox = new VBox(5, gameName, gamePlayers, gameTime);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(10, imageContainer, textBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("card");
        card.setMaxWidth(256);
        card.setMinWidth(256);
        card.setUserData(g.getGameId());

        FlowPane.setMargin(card, new Insets(10, 5, 0, 5));
        return card;

    }
}

