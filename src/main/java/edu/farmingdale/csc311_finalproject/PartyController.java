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
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;

public class PartyController implements Initializable {

    @FXML
    private Label homeBtn;
    @FXML
    private Label partiesBtn;
    @FXML
    private Label friendsBtn;
    @FXML
    private Label gamesBtn;
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
    private User currentUser;
    private Party party;
    private final ObservableList<User> guests =  FXCollections.observableArrayList();
    private final ObservableSet<Game> games =  FXCollections.observableSet();
    private final ObservableList<GameWithVotes> selectedGames =  FXCollections.observableArrayList();
    private final List<Game> userSelectedGames = new ArrayList<>();
    //popup variables
    private final List <User> friends = new ArrayList<>();
    private Popup guestPopup;
    private User selectedGuest;
    private Node selectedCard;

    public PartyController(Party party){
        this.party = party;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //keep things centered when app size changes
        gamesLabel.maxWidthProperty().bind(gamesContainer.widthProperty());
        selectedGamesLabel.maxWidthProperty().bind(selectedGamesContainer.widthProperty());
        gamesListContainer.setFitToWidth(true);
        gamesList.prefWidthProperty().bind(gamesListContainer.widthProperty());

        //create popup for adding guests
        guestPopup = new Popup();

        //navbar handler
        NavBarHandler.setupNav(homeBtn, gamesBtn,friendsBtn,partiesBtn);

        //todo switch to session user
        try {
            currentUser = sendGET(getUserUrl(2), new TypeReference<User>() {});
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
            try {
                friends.addAll(Objects.requireNonNull(sendGET(getUserFriends(currentUser.getUserId()), new TypeReference<ArrayList<User>>() {})));

            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            currentUser = null;//TODO FIX THIS!!!!!!!
            e.printStackTrace();
        }


        //todo move to a different initialize method that gets called with the party from the user page or a page that displays all parties
//        try {
//           party = sendGET(ApiClient.getParty(1), new TypeReference<Party>() {});
//        } catch (IOException e) {
//           e.printStackTrace();
//           party = new Party(-1L,"Party Name", null, "party loc");
//        }

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

        for (User u:guests){
            HBox card = createGuestCard(u);
            guestList.getChildren().add(card);

            try {
                games.addAll(Objects.requireNonNull(sendGET(getUserGames(u.getUserId()), new TypeReference<List<Game>>() {
                })));
                userSelectedGames.addAll(Objects.requireNonNull(sendGET(getUserSelectedGames(party.getPartyId(),currentUser.getUserId()), new TypeReference<ArrayList<Game>>() {})));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Game g : games) {
            HBox card = createGameCard(g);
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

    public HBox createGuestCard(User u){
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

        //guestList.getChildren().add(card);
        return card;
    }

    public HBox createGameCard(Game g){
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
                    sendDELETE(deselectGame(party.getPartyId(), id, currentUser.getUserId()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                selectedGamesList.getChildren().clear();
                updateSelectedGames();

            } else {
                styleClasses.add("selected");
                try {
                    sendPOST(selectGame(party.getPartyId(), id, currentUser.getUserId()), null, new TypeReference<Void>() {
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                selectedGamesList.getChildren().clear();
                updateSelectedGames();

            }
        });
        if(userSelectedGames.contains(g)){
            card.getStyleClass().add("selected");
        }

        FlowPane.setMargin(card, new Insets(10, 5, 0, 5));
        return card;

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

    @FXML
    void addGuest(MouseEvent event) {
        if (!guestPopup.isShowing()) {

            VBox friendsList = new VBox();
            ScrollPane friendsListContainer = new ScrollPane(friendsList);
            friendsList.getStyleClass().add("friends-popup-list");
            friendsListContainer.setPrefSize(200, 200);
            friendsList.setPrefWidth(194);
            friendsList.setMaxWidth(194);
            //guestList.prefHeightProperty().bind(guestListContainer.heightProperty());
            Button cancel = new Button("cancel");
            cancel.setOnAction(e->{
                guestPopup.hide();
            });
            Button add = new Button("add");

            add.setOnAction(e->{
                if (selectedGuest != null && !guests.contains(selectedGuest)){
                    guests.add(selectedGuest);
                    guestList.getChildren().add(createGuestCard(selectedGuest));
                    try {
                        sendPOST(addUserToParty(selectedGuest.getUserId(), party.getPartyId(), false), null, new TypeReference<Void>() {
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        List<Game> friendGames = sendGET(getUserGames(selectedGuest.getUserId()), new TypeReference<List<Game>>() {});
                        for(Game g:friendGames){
                            if(!games.contains(g)) {
                                games.add(g);
                                gamesList.getChildren().add(createGameCard(g));
                            }
                        }
                    } catch (IOException | NullPointerException  ex ) {
                        ex.printStackTrace();
                    }
                }
                selectedGuest = null;
                if (selectedCard != null) {
                    selectedCard.getStyleClass().remove("selected-card");
                    selectedCard = null;
                }
                guestPopup.hide();
            });
            cancel.getStyleClass().add("friends-popup-button");
            add.getStyleClass().add("friends-popup-button");
            HBox buttonContainer = new HBox(20, cancel, add);
            buttonContainer.setAlignment(Pos.CENTER);
            VBox popup = new VBox(10, friendsListContainer, buttonContainer);
            popup.setMargin(buttonContainer, new Insets(10, 0, 20, 0));
            popup.getStyleClass().add("friends-popup");
            for (User g : friends) {
                if(!guests.contains(g)) {
                    HBox card = createGuestCard(g);
                    card.getStyleClass().add("friend-card");
                    card.setUserData(g.getUserId());
                    //add event listener
                    card.setOnMouseClicked(e -> {
                        long id = (Long) card.getUserData();
                        selectedGuest = friends.stream()
                                .filter(f -> f.getUserId() == id)
                                .findFirst()
                                .orElse(null);

                        if (selectedCard != null) {
                            selectedCard.getStyleClass().add("selected-friend");
                        }
                        selectedCard = card;
                        selectedCard.getStyleClass().add("selected-friend");
                        System.out.println(selectedCard == null);
                        if (selectedCard != null) {
                            System.out.println(selectedCard.getStyleClass());
                        }
                    });
                    friendsList.getChildren().add(card);
                }
            }
            guestPopup.getContent().add(popup);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            guestPopup.show(stage, event.getScreenX(), event.getScreenY());
        }

    }


}