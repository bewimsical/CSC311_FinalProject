package edu.farmingdale.csc311_finalproject;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavBarHandler {
    private NavBarHandler(){}

    public static void setupNav(Label home, Label games, Label friends, Label parties, MenuItem logout){
        home.setOnMouseClicked(e -> goToProfile(e.getSource(), "ProfilePage.fxml"));
        games.setOnMouseClicked(e -> switchScene(e.getSource(), "games-view.fxml"));
        friends.setOnMouseClicked(e -> switchScene(e.getSource(), "friends-view.fxml"));
        parties.setOnMouseClicked(e -> switchScene(e.getSource(), "all-parties-view.fxml"));
        logout.setOnAction(e -> {
            logout(e, "LoginPage.fxml");
            Session.getInstance().clearSession();
        });

    }

    public static Image setupNavImage(){
        String imgUrl = Session.getInstance().getUser().getProfilePicUrl();
        System.out.println(imgUrl);
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
        return profileImg;
    }

    private static void switchScene(Object source, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(NavBarHandler.class.getResource(fxmlFile));
            Stage stage = (Stage)((Node)source).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(NavBarHandler.class.getResource("styles/party-style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void goToProfile(Object source, String fxmlFile){
        try {
            FXMLLoader loader = new FXMLLoader(AllPartiesController.class.getResource("ProfilePage.fxml"));
            loader.setControllerFactory(param -> new ProfilePageController());
            Parent root = loader.load();
            Stage stage = (Stage)((Node)source).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(NavBarHandler.class.getResource("styles/party-style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logout(Event e, String fxmlFile){
        try {
            // Get the focused window, since MenuItem is not part of the scene graph
            Stage stage = (Stage) Stage.getWindows().stream()
                    .filter(Window::isFocused)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No focused window"));

            Parent root = FXMLLoader.load(NavBarHandler.class.getResource(fxmlFile));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(NavBarHandler.class.getResource("styles/party-style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private static boolean checkImage(String imgUrl){
        String pattern = "^images";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(imgUrl);
        System.out.println(m.lookingAt());
        return m.lookingAt();
    }
}
