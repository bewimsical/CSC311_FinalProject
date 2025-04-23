package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static edu.farmingdale.csc311_finalproject.ApiClient.getPartyUsers;
import static edu.farmingdale.csc311_finalproject.ApiClient.sendGET;

public class PartyController implements Initializable {

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
    
    private Party party;
    private final ObservableList<User> guests =  FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
           party = sendGET(ApiClient.getParty(2), new TypeReference<Party>() {});
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