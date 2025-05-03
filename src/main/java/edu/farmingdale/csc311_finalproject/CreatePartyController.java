package edu.farmingdale.csc311_finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import edu.farmingdale.getgame.dto.PartyDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreatePartyController {

    @FXML private TextField partyNameField;
    @FXML private DatePicker partyDatePicker;
    @FXML private TextField partyTimeField;
    @FXML private TextField locationField;

    @FXML
    private void handleSubmitParty() {
        try {
            String name = partyNameField.getText();
            LocalDate date = partyDatePicker.getValue();
            LocalTime time = LocalTime.parse(partyTimeField.getText()); // assume valid format
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            String location = locationField.getText();

            PartyDto dto = new PartyDto(name, dateTime, location);
            GameNightAPI.createParty(dto); // <- your networking logic
            ((Stage) partyNameField.getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace(); // add proper error alert
        }
    }
}
