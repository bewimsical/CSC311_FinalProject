package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;

public class CreatePartyController implements Initializable {

    @FXML private TextField partyNameField;
    @FXML private DatePicker partyDatePicker;
    @FXML private ComboBox<String> hourCombo;
    @FXML private ComboBox<String> minuteCombo;
    @FXML private ComboBox<String> ampmCombo;
    @FXML private TextField locationField;
    @FXML private Button createButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        hourCombo.setItems(FXCollections.observableArrayList(generateNumberStrings(1, 12)));
        minuteCombo.setItems(FXCollections.observableArrayList("00", "15", "30", "45"));
        ampmCombo.setItems(FXCollections.observableArrayList("AM", "PM"));

        hourCombo.getSelectionModel().select("7");
        minuteCombo.getSelectionModel().select("00");
        ampmCombo.getSelectionModel().select("PM");

        createButton.setOnAction(event -> createParty());
    }

    private void createParty() {
        String name = partyNameField.getText();
        LocalDate date = partyDatePicker.getValue();
        String hourStr = hourCombo.getValue();
        String minuteStr = minuteCombo.getValue();
        String ampm = ampmCombo.getValue();
        String loc = locationField.getText();

        if (name.isEmpty() || date == null || hourStr == null || minuteStr == null || ampm == null || loc.isEmpty()) {
            showAlert("Please fill in all fields.");
            return;
        }

        int hour = Integer.parseInt(hourStr);
        int minute = Integer.parseInt(minuteStr);
        if (ampm.equals("PM") && hour != 12) hour += 12;
        if (ampm.equals("AM") && hour == 12) hour = 0;

        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute));

        Party newParty = new Party(name, dateTime, loc);
        try {
            sendPOST(getAllPartiesUrl(), newParty, new TypeReference<Party>() {});
            Stage stage = (Stage) createButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert("Error creating party: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }

    private static java.util.List<String> generateNumberStrings(int start, int end) {
        java.util.List<String> list = new java.util.ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(String.format("%02d", i));
        }
        return list;
    }
}
