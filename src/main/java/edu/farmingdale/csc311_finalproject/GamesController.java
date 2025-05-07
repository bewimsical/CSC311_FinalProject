package edu.farmingdale.csc311_finalproject;


import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import edu.farmingdale.csc311_finalproject.SearchGame;

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
    private MenuItem logout;
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

    private User currentUser = Session.getInstance().getUser();
    private final ObservableSet<Game> games =  FXCollections.observableSet();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //navbar handler
        NavBarHandler.setupNav(homeBtn, gamesBtn,friendsBtn,partiesBtn, logout);

        Image image = NavBarHandler.setupNavImage();

        ImageView profilePic = new ImageView(image);
        profilePic.setFitWidth(circle_view.getRadius() * 2);
        profilePic.setFitHeight(circle_view.getRadius() * 2);
        profilePic.setClip(new Circle(circle_view.getRadius(), circle_view.getRadius(), circle_view.getRadius()));

        // Add to StackPane (on top of the Circle)
        profileContainer.getChildren().add(profilePic);

        // Set username
        usernameLabel.setText(currentUser.getUsername());

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
        Image gameImage;
        try {
            gameImage = new Image(gameImg, true);
        }catch (Exception e){
            gameImage = new Image(Objects.requireNonNull(getClass().getResource("images/d20_logo_sky.PNG")).toExternalForm());
        }

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

    private Popup searchPopup = new Popup();
    private SearchGame selectedSearchGame;


    private void displaySearchResults(List<SearchGame> results) {
        VBox resultBox = new VBox(5);
        resultBox.setPadding(new Insets(10));
        resultBox.getStyleClass().add("friends-popup-list");

        for (SearchGame game : results) {
            Label resultLabel = new Label(game.getName() + " (" + game.getYear() + ")");
            resultLabel.getStyleClass().add("game-search-result");

            resultLabel.setOnMouseClicked(e -> {
                selectedSearchGame = game;
                try {
                    Game fullGame = ApiClient.sendGET(
                            ApiClient.getGameUrl(selectedSearchGame.getId()),
                            new TypeReference<Game>() {}
                    );
                    ApiClient.sendPOST(
                            ApiClient.addGameToUser(currentUser.getUserId(), fullGame.getGameId()),
                            null,
                            new TypeReference<Void>() {}
                    );
                    if(!games.contains(fullGame)) {
                        games.add(fullGame);
                        gamesList.getChildren().add(createGameCard(fullGame));
                    }
                    PartyController partyController = Session.getInstance().getPartyController();
                    if (partyController != null) {
                        partyController.addGameToPartyList(fullGame);
                    }
                    searchPopup.hide();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            resultBox.getChildren().add(resultLabel);
        }

        ScrollPane scroll = new ScrollPane(resultBox);
        scroll.setPrefSize(250, 200);
        scroll.getStyleClass().add("friends-popup");
        scroll.getStylesheets().add(getClass().getResource("styles/party-style.css").toExternalForm());
        searchPopup.getContent().clear();
        searchPopup.getContent().add(scroll);
        searchPopup.setAutoHide(true);
        Stage stage = (Stage) searchField.getScene().getWindow();
        searchPopup.show(stage, searchField.localToScreen(0, 0).getX(), searchField.localToScreen(0, 0).getY() + 40);
    }

    public void addGameToPartyList(Game g) {
        System.out.println("Attempting to add game: " + g.getGame_name());
        if (!games.contains(g)) {
            games.add(g);
            gamesList.getChildren().add(createGameCard(g));
            System.out.println("Game added to PartyController!");
        } else {
            System.out.println("Game already exists in PartyController.");
        }
    }


    @FXML
    void searchGame() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) return;

        try {
            List<SearchGame> results = ApiClient.sendGET(
                    ApiClient.searchGameUrl(query),
                    new TypeReference<List<SearchGame>>() {}
            );

            displaySearchResults(results);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}

