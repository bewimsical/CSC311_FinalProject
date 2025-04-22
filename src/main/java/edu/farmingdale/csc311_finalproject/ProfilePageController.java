package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;

public class ProfilePageController {

    @FXML
    private ImageView profilePic;

    @FXML
    private Label welcomeText;

    //@FXML
    //private Text usernameLabel;

    @FXML
    private Label emailLabel;

private User user;

    @FXML
    private Label usernameLabel; // or Text if you used that!

    private final ObservableList<Game> gameData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        try {
            User user = sendGET(getUserUrl(1), new TypeReference<User>() {});
            sendPOST(createUserUrl(),
                    new User("samsj", "Josh", "Samson", "samsj@farmingdale.edu", "/images/wizard_cat.PNG", "JoshSamson"),
                    new TypeReference<User>() {});

            if (user != null) {
                usernameLabel.setText(user.getUsername());
                // final ObservableList<Game> gameData = FXCollections.observableArrayList();
                List<Game> games = sendGET(getUserGames(1), new TypeReference<List<Game>>() {});
                gameData.addAll(games);
                String imagePath = "/edu/farmingdale/csc311_finalproject/images"; // Folder where images are stored
                File imageFolder = new File(getClass().getResource(imagePath).toURI());
                File[] imageFiles = imageFolder.listFiles((dir, name) -> name.endsWith(".PNG") || name.endsWith(".jpg") || name.endsWith(".jpeg"));

                if (imageFiles != null && imageFiles.length > 0) {
                    // Pick a random image file
                    Random rand = new Random();
                    File randomImage = imageFiles[rand.nextInt(imageFiles.length)];

                    // Load the selected image
                    Image image = new Image(randomImage.toURI().toString());
                    profilePic.setImage(image);
                }

                for (Game g : games) {
                    System.out.println(g.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Or handle better
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    /**
    public void addGameToUser(long userId, int gameId) {
        try {
            // 1. Call the API to add the game
            sendPOST(addGameToUser(userId, gameId), null, new TypeReference<Void>() {});

            // 2. Fetch the full game info
            Game newGame = sendGET(getGameUrl(gameId), new TypeReference<Game>() {});

            // 3. Add to observable list if not already present
            if (newGame != null && !gameData.contains(newGame)) {
                gameData.add(newGame);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
**/

}

