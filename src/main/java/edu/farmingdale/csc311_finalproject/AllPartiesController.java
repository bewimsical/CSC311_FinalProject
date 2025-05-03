package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;

public class AllPartiesController implements Initializable {
    @FXML
    private Circle circle_view;
    @FXML
    private Label friendsBtn;
    @FXML
    private Label gamesBtn;
    @FXML
    private VBox partiesContainer;
    @FXML
    private Label partiesLabel;
    @FXML
    private VBox partiesList;
    @FXML
    private ScrollPane partiesListContainer;
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
    private final ObservableSet<Party> parties =  FXCollections.observableSet();
    private final ObservableSet<Party> upcomingParties = FXCollections.observableSet();
    private final ObservableSet<Party> pastParties = FXCollections.observableSet();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //navbar handler
        NavBarHandler.setupNav(homeBtn, gamesBtn, friendsBtn, partiesBtn);

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
            parties.addAll(Objects.requireNonNull(sendGET(getUserParties(currentUser.getUserId()), new TypeReference<List<Party>>() {
            })));


            for (Party p : parties) {
                HBox card = createPartyCard(p);
                partiesList.getChildren().add(card);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HBox createPartyCard(Party p){
        Label day = new Label(formatDay(p.getPartyDate()));
        day.getStyleClass().add("month-text");
        day.setTextAlignment(TextAlignment.CENTER);
        day.setAlignment(Pos.CENTER);
        Label month = new Label(formatMonth(p.getPartyDate()));
        month.getStyleClass().add("month-label");
        month.setTextAlignment(TextAlignment.CENTER);
        month.setAlignment(Pos.CENTER);
        VBox dateContainer = new VBox(month, day);
        day.maxWidthProperty().bind(dateContainer.widthProperty());
        month.maxWidthProperty().bind(dateContainer.widthProperty());
        dateContainer.setAlignment(Pos.CENTER);
        dateContainer.setMaxWidth(100);
        dateContainer.setMinWidth(100);


        Label partyName = new Label(p.getPartyName());
        partyName.setWrapText(true);
        partyName.setMaxWidth(250); // adjust based on design
        partyName.setTextAlignment(TextAlignment.LEFT);
        partyName.setAlignment(Pos.CENTER_LEFT);
        partyName.getStyleClass().add("party-name-text");

        Label time = new Label(formatTime(p.getPartyDate()));
        Label location = new Label(p.getLocation());
        time.getStyleClass().add("party-info-text");
        location.getStyleClass().add("party-info-text");


        VBox textBox = new VBox(5, partyName, time, location);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(10,dateContainer, textBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(370);
        card.setMinWidth(370);


        HBox sgContainer = new HBox(5, dateContainer, card);
        sgContainer.getStyleClass().add("card");
        sgContainer.getStyleClass().add("select-card");
        sgContainer.setUserData(p.getPartyId());
        sgContainer.setOnMouseClicked(event -> {
            System.out.println(p.getPartyId());

            Long id = (Long)sgContainer.getUserData();
            try {
                Party party = sendGET(getParty(id), new TypeReference<Party>() {});
                try {
                    FXMLLoader loader = new FXMLLoader(AllPartiesController.class.getResource("party-view.fxml"));
                    loader.setControllerFactory(param -> new PartyController(party));
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
        VBox.setMargin(sgContainer, new Insets(10, 0, 0, 0));
        return sgContainer;

    }

    private String formatMonth(LocalDateTime date){
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.US);
            return date.format(formatter);
        }
        return "no date";
    }
    private String formatDay(LocalDateTime date){
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
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
