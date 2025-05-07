package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreatePartyController {

    @FXML
    private TextField partyNameField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> hourBox;

    @FXML
    private ComboBox<String> minuteBox;

    @FXML
    private ComboBox<String> amPmBox;

    @FXML
    private TextField locationField;

    @FXML
    private Button createBtn;

    private Long loggedInUserId = Session.getInstance().getUser().getUserId();

    @FXML
    private void initialize() {
        for (int i = 1; i <= 12; i++) hourBox.getItems().add(String.format("%02d", i));
        for (int i = 0; i < 60; i++) minuteBox.getItems().add(String.format("%02d", i));
        amPmBox.getItems().addAll("AM", "PM");
    }

    @FXML
    private void handleCreateParty() {
        String name = partyNameField.getText();
        LocalDate date = datePicker.getValue();
        String hour = hourBox.getValue();
        String minute = minuteBox.getValue();
        String amPm = amPmBox.getValue();
        String location = locationField.getText();

        if (name.isEmpty() || date == null || hour == null || minute == null || amPm == null || location.isEmpty()) {
            showAlert("All fields are required.");
            return;
        }

        int hourInt = Integer.parseInt(hour);
        if (amPm.equals("PM") && hourInt != 12) hourInt += 12;
        if (amPm.equals("AM") && hourInt == 12) hourInt = 0;

        partyNameField.getStyleClass().add("create-party-text-field");
        locationField.getStyleClass().add("create-party-text-field");
        hourBox.getStyleClass().add("combo-box");
        minuteBox.getStyleClass().add("combo-box");
        amPmBox.getStyleClass().add("combo-box");

        LocalDateTime fullDateTime = LocalDateTime.of(date, LocalTime.of(hourInt, Integer.parseInt(minute)));


        String json = String.format(
                "{\"party_name\": \"%s\", \"party_date\": \"%s\", \"location\": \"%s\"}",
                name, fullDateTime.toString(), location
        );

        try {
            URL url = new URL(ApiClient.getBaseApiUrl() + "/parties/create/" + loggedInUserId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code == 200 || code == 201) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                Party createdParty = mapper.readValue(conn.getInputStream(), Party.class);

                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Party Created");
                    alert.setHeaderText(null);
                    alert.setContentText("Party created successfully!");
                    alert.showAndWait();

                    ((Stage) createBtn.getScene().getWindow()).close();
                });
            }



        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Create Party");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUserId(Long id) {
        this.loggedInUserId = id;
    }
}
