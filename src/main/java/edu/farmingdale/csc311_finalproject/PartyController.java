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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;

public class PartyController implements Initializable {

    @FXML
    private Circle circle_view;
    @FXML
    private FlowPane gamesList;
    @FXML
    private ScrollPane gamesListContainer;
    @FXML
    private Label partyDate;
    @FXML
    private Label partyLoc;
    @FXML
    private Label partyTime;
    @FXML
    private Label partyNameLabel;
    @FXML
    private Label guestsLabel;
    @FXML
    private VBox guestList;
    @FXML
    private MenuButton usernameLabel;
    @FXML
    private StackPane profileContainer;
    @FXML
    private VBox gamesContainer;
    @FXML
    private Label gamesLabel;
    @FXML
    private VBox selectedGamesContainer;
    @FXML
    private Label selectedGamesLabel;
    @FXML
    private FlowPane selectedGamesList;
    
    private Party party;
    private final ObservableList<User> guests =  FXCollections.observableArrayList();
    private final ObservableSet<Game> games =  FXCollections.observableSet();
    private final ObservableList<GameWithVotes> selectedGames =  FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gamesLabel.maxWidthProperty().bind(gamesContainer.widthProperty());
        selectedGamesLabel.maxWidthProperty().bind(selectedGamesContainer.widthProperty());
        //todo switch to session user
        try {
            User currentUser = sendGET(getUserUrl(1), new TypeReference<User>() {});
            String img = currentUser.getProfilePicUrl() != null ? currentUser.getProfilePicUrl() : "images/wizard_cat.PNG";

// Load and resize image
            Image image = new Image(getClass().getResource(img).toExternalForm());
            ImageView profilePic = new ImageView(image);
            profilePic.setFitWidth(circle_view.getRadius() * 2);
            profilePic.setFitHeight(circle_view.getRadius() * 2);
            profilePic.setClip(new Circle(circle_view.getRadius(), circle_view.getRadius(), circle_view.getRadius()));

// Add to StackPane (on top of the Circle)
            profileContainer.getChildren().add(profilePic);

// Set username
            usernameLabel.setText(currentUser.getUsername());

        } catch (IOException e) {
            e.printStackTrace();
        }

        gamesListContainer.setFitToWidth(true);
        gamesList.prefWidthProperty().bind(gamesListContainer.widthProperty());
        //todo move to a different initialize method that gets called with the party from the user page or a page that displays all parties
        try {
           party = sendGET(ApiClient.getParty(1), new TypeReference<Party>() {});
        } catch (IOException e) {
           e.printStackTrace();
           party = new Party(-1L,"Party Name", null, "party loc");
        }

        partyNameLabel.setText(party.getPartyName());
        partyNameLabel.setPrefHeight(Region.USE_COMPUTED_SIZE);
        partyLoc.setText(party.getLocation());
        partyDate.setText(formatDate(party.getPartyDate()));
        partyTime.setText(formatTime(party.getPartyDate()));

        try {
            guests.addAll(Objects.requireNonNull(sendGET(getPartyUsers(party.getPartyId()), new TypeReference<List<User>>() {
            })));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //todo extract to a method
        for (User u:guests){
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
            guestList.getChildren().add(card);

            try {
                games.addAll(Objects.requireNonNull(sendGET(getUserGames(u.getUserId()), new TypeReference<List<Game>>() {
                })));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //todo extract to method.
        for (Game g : games) {
            String gameImg = g.getImgUrl();
            Image gameImage = new Image(gameImg, true);
            ImageView gamePic = new ImageView(gameImage);
            gamePic.setFitHeight(75);
            gamePic.setFitWidth(75);
            gamePic.setPreserveRatio(true);
            gamePic.setSmooth(true);

            StackPane imageContainer = new StackPane(gamePic);
            imageContainer.setPrefSize(75, 75);
            imageContainer.setMaxSize(75, 75);

            Label gameName = new Label(g.getGame_name());
            gameName.setWrapText(true);
            gameName.setMaxWidth(140); // adjust based on design
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
            card.setUserData(g.getGameId());
            card.setOnMouseClicked(event -> {
                ObservableList<String> styleClasses = card.getStyleClass();
                int id = (Integer) card.getUserData();
                if (styleClasses.contains("selected")) {
                    styleClasses.remove("selected");
                    try {
                        sendDELETE(deselectGame(party.getPartyId(), id));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    selectedGamesList.getChildren().clear();
                    updateSelectedGames();

                } else {
                    styleClasses.add("selected");
                    try {
                        sendPOST(selectGame(party.getPartyId(), id), null, new TypeReference<Void>() {
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    selectedGamesList.getChildren().clear();
                    updateSelectedGames();

                }
            });

            FlowPane.setMargin(card, new Insets(10, 5, 0, 5));
            gamesList.getChildren().add(card);
        }

        updateSelectedGames();


    }

    private void updateSelectedGames(){
        selectedGames.clear();
        try {
            selectedGames.addAll(Objects.requireNonNull(sendGET(getSelectedGames(party.getPartyId()), new TypeReference<List<GameWithVotes>>() {
            })));
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(GameWithVotes g:selectedGames){
            Label gameVotes = new Label(String.valueOf(g.getVotes()));
            gameVotes.getStyleClass().add("votes-text");
            gameVotes.setTextAlignment(TextAlignment.CENTER);
            gameVotes.setAlignment(Pos.CENTER);
            String lbl = g.getVotes() == 1 ? "vote" : "votes";
            Label gameVotesLabel = new Label(lbl);
            gameVotesLabel.getStyleClass().add("votes-label");
            gameVotesLabel.setTextAlignment(TextAlignment.CENTER);
            gameVotesLabel.setAlignment(Pos.CENTER);
            VBox votesContainer = new VBox(gameVotes, gameVotesLabel);
            gameVotes.maxWidthProperty().bind(votesContainer.widthProperty());
            gameVotesLabel.maxWidthProperty().bind(votesContainer.widthProperty());
            votesContainer.setAlignment(Pos.CENTER);
            votesContainer.setMaxWidth(40);
            votesContainer.setMinWidth(40);
//            votesContainer.getStyleClass().add("card");


            String gameImg = g.getImgUrl();
            Image gameImage = new Image(gameImg, true);
            ImageView gamePic = new ImageView(gameImage);
            gamePic.setFitHeight(75);
            gamePic.setFitWidth(75);
            gamePic.setPreserveRatio(true);
            gamePic.setSmooth(true);

            StackPane imageContainer = new StackPane(gamePic);
            imageContainer.setPrefSize(75, 75);
            imageContainer.setMaxSize(75, 75);

            Label gameName = new Label(g.getGame_name());
            gameName.setWrapText(true);
            gameName.setMaxWidth(150); // adjust based on design
            gameName.setTextAlignment(TextAlignment.LEFT);
            gameName.setAlignment(Pos.CENTER_LEFT);
            gameName.getStyleClass().add("game-name-text");

            Label gamePlayers = new Label(g.getMinPlayers() + "-" + g.getMaxPlayers() + " players");
            Label gameTime = new Label(g.getPlayTime() + " min");
            gamePlayers.getStyleClass().add("info-text");
            gameTime.getStyleClass().add("info-text");

            VBox textBox = new VBox(5, gameName, gamePlayers, gameTime);
            textBox.setAlignment(Pos.CENTER_LEFT);

            HBox card = new HBox(10,votesContainer, imageContainer, textBox);
            card.setAlignment(Pos.CENTER_LEFT);
            //card.getStyleClass().add("card");
            card.setMaxWidth(215);
            card.setMinWidth(215);

            HBox sgContainer = new HBox(5, votesContainer, card);
            sgContainer.getStyleClass().add("card");
            sgContainer.getStyleClass().add("select-card");
            FlowPane.setMargin(sgContainer, new Insets(10, 5, 0, 5));

            selectedGamesList.getChildren().add(sgContainer);
        }
    }

    private String formatDate(LocalDateTime date){
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            return date.format(formatter);
        }
        return "no date";
    }
    private String formatTime(LocalDateTime date){
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
            return date.format(formatter);
        }
        return "no time";
    }


}