package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static edu.farmingdale.csc311_finalproject.ApiClient.*;

public class AllPartiesController implements Initializable {

    @FXML
    private ImageView nextMonth;
    @FXML
    private ImageView prevMonth;
    @FXML
    private Accordion accordianPane;
    @FXML
    private TitledPane selectedDayPane;
    @FXML
    private TitledPane pastPartiesPane;
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
    private MenuButton usernameLabel;
    @FXML
    private GridPane calendarGrid;
    @FXML
    private Label monthLabel;
    @FXML
    private VBox pastPartiesList;
    @FXML
    private VBox selectedDayList;

    private User currentUser = Session.getInstance().getUser();
    private final ObservableSet<Party> parties =  FXCollections.observableSet();
    private final ObservableList<Party> upcomingParties = FXCollections.observableArrayList();
    private final ObservableList<Party> pastParties = FXCollections.observableArrayList();
    private final List<Party> selectedDayParties = new ArrayList<>();
    private Map<LocalDate, List<Party>> partiesByDate = new TreeMap<>();
    private StackPane selectedDay = null;
    YearMonth currentMonth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //navbar handler
        NavBarHandler.setupNav(homeBtn, gamesBtn, friendsBtn, partiesBtn);

//        String img = currentUser.getProfilePicUrl() != null ? currentUser.getProfilePicUrl() : "images/wizard_cat.PNG";
//        Image image;
//        // Load and resize image for nav bar
//        try {
//            image = new Image(Objects.requireNonNull(getClass().getResource(img)).toExternalForm());
//        } catch (Exception e) {
//            image = new Image(Objects.requireNonNull(getClass().getResource("images/wizard_cat.PNG")).toExternalForm());
//        }
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
            parties.addAll(Objects.requireNonNull(sendGET(getUserParties(currentUser.getUserId()), new TypeReference<List<Party>>() {
            })));
            pastParties.addAll(parties.stream()
                    .filter(p -> p.getPartyDate().isBefore(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Party::getPartyDate).reversed())
                    .toList());
            upcomingParties.addAll(parties.stream()
                    .filter(p -> p.getPartyDate().isAfter(LocalDateTime.now()))
                    .sorted(Comparator.comparing(Party::getPartyDate))
                    .toList());

            for (Party p : upcomingParties) {
                HBox card = createPartyCard(p);
                partiesList.getChildren().add(card);
            }
            for (Party p : pastParties) {
                HBox card = createPastPartiesCard(p);
                pastPartiesList.getChildren().add(card);
            }

            partiesByDate.putAll( parties.stream()
                    .collect(Collectors.groupingBy(p -> p.getPartyDate().toLocalDate())));

        } catch (IOException e) {
            e.printStackTrace();
        }
        currentMonth = YearMonth.now();
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int year = currentMonth.getYear();
        String display = monthName + " " + year;
        monthLabel.setText(display);
        fillCalendar(currentMonth);

        nextMonth.setOnMouseClicked(e->{
            currentMonth = currentMonth.plusMonths(1);
            fillCalendar(currentMonth);
            final String nextMonthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            final int nextMonthYear = currentMonth.getYear();
            final String nextMonthDisplay = nextMonthName + " " + nextMonthYear;
            monthLabel.setText(nextMonthDisplay);
        });
        prevMonth.setOnMouseClicked(e->{
            currentMonth = currentMonth.minusMonths(1);
            fillCalendar(currentMonth);
            final String prevMonthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            final int prevMonthYear = currentMonth.getYear();
            final String prevMonthDisplay = prevMonthName + " " + prevMonthYear;
            System.out.println("prev: "+prevMonthDisplay);
            monthLabel.setText(prevMonthDisplay);
        });


    }

    public HBox createPartyCard(Party p){
        Label day = new Label(formatDay(p.getPartyDate()));
        day.getStyleClass().add("month-text");
        day.setTextAlignment(TextAlignment.CENTER);
        day.setAlignment(Pos.CENTER);
        System.out.println(day.getStyleClass());
        Label month = new Label(formatMonth(p.getPartyDate()));
        month.getStyleClass().add("month-label");
        month.setTextAlignment(TextAlignment.CENTER);
        month.setAlignment(Pos.CENTER);
        day.setPadding(Insets.EMPTY);
        month.setPadding(Insets.EMPTY);
        System.out.println(month.getStyleClass());
        VBox dateContainer = new VBox(month, day);
        day.maxWidthProperty().bind(dateContainer.widthProperty());
        month.maxWidthProperty().bind(dateContainer.widthProperty());
        dateContainer.setAlignment(Pos.CENTER);
        dateContainer.setMaxWidth(100);
        dateContainer.setMinWidth(100);
        dateContainer.setSpacing(0);


        Label partyName = new Label(p.getPartyName());
        partyName.setWrapText(true);
        partyName.setMaxWidth(250); // adjust based on design
        partyName.setTextAlignment(TextAlignment.LEFT);
        partyName.setAlignment(Pos.CENTER_LEFT);
        partyName.getStyleClass().add("party-name-text");

        Image clockImg = new Image(getClass().getResource("images/clock.png").toExternalForm());
        Image houseImg = new Image(getClass().getResource("images/house.png").toExternalForm());
        ImageView clock = new ImageView(clockImg);
        ImageView house = new ImageView(houseImg);
        double size = 15;
        clock.setFitWidth(size);
        clock.setFitHeight(size);
        house.setFitWidth(size);
        house.setFitHeight(size);
        Label time = new Label(formatTime(p.getPartyDate()));
        Label location = new Label(p.getLocation());
        time.getStyleClass().add("party-info-text");
        location.getStyleClass().add("party-info-text");

        HBox timeContainer = new HBox(5, clock, time);
        HBox locationContainer = new HBox(5, house, location);
        timeContainer.setAlignment(Pos.CENTER_LEFT);
        locationContainer.setAlignment(Pos.CENTER_LEFT);


        VBox textBox = new VBox(5, partyName, timeContainer, locationContainer);
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

    private void  fillCalendar(YearMonth month){
        calendarGrid.getChildren().clear();

        String[] days = {"Su", "Mo", "Tu", "We", "Tr", "Fr", "Sa"};
        for (int i = 0; i < days.length; i++) {
            Label header = new Label(days[i]);
            header.getStyleClass().add("calendar-header");
            calendarGrid.add(header, i, 0);
            header.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // allow it to expand
            header.setAlignment(Pos.CENTER);
        }

        LocalDate first = month.atDay(1);
        int col = first.getDayOfWeek().getValue()% 7;
        int row = 1;

        for (int day = 1; day <= month.lengthOfMonth(); day++){
            LocalDate date = month.atDay(day);
            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.getStyleClass().add("calendar-header");
            dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

            Circle highlight = new Circle(12);
            highlight.getStyleClass().add("calendar-highlight");
            if(partiesByDate.containsKey(date)){
                highlight.getStyleClass().add("has-event");
            }
            if (date.equals(LocalDate.now())){
                highlight.getStyleClass().add("today");
            }

            StackPane daycell = new StackPane(highlight, dayLabel);
            daycell.setPrefSize(12, 12);
            daycell.getStyleClass().add("day-cell");


            daycell.setOnMouseClicked(e->{
                showEventsForDate(date);
                if(selectedDay != null){
                    selectedDay.getStyleClass().remove("selected");
                }
                selectedDay = daycell;
                daycell.getStyleClass().add("selected");
                System.out.println(dayLabel.getText());
            });

            calendarGrid.add(daycell, col, row);
            col++;
            if (col > 6){
                col = 0;
                row++;
            }

        }
    }

    private void showEventsForDate(LocalDate date){
        accordianPane.setExpandedPane(selectedDayPane);
        selectedDayParties.clear();
        selectedDayList.getChildren().clear();
        selectedDayParties.addAll(parties.stream()
                .filter(p -> p.getPartyDate().toLocalDate().equals(date))
                .sorted(Comparator.comparing(Party::getPartyDate))
                .toList());
        if (selectedDayParties.isEmpty()){
            HBox card = createNoPartiesCard();
            selectedDayList.getChildren().add(card);

        }else{
            for(Party p:selectedDayParties){
                HBox card = createSelectedDayPartyCard(p);
                selectedDayList.getChildren().add(card);
            }
        }
    }

    private HBox createNoPartiesCard(){
        Label label = new Label("NO PARTIES");
        label.getStyleClass().add("selected-party-time-text");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setAlignment(Pos.CENTER);
        HBox card = new HBox(10,label);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(260);
        card.setMinWidth(260);
        card.getStyleClass().add("card");
        card.getStyleClass().add("selected-party-card");
        label.maxWidthProperty().bind(card.widthProperty());
        VBox.setMargin(card, new Insets(10, 0, 0, 0));
        return card;
    }
    private HBox createSelectedDayPartyCard(Party p){
        Label time = new Label(formatTime(p.getPartyDate()));
        time.getStyleClass().add("selected-party-time-text");

        Label partyName = new Label(p.getPartyName());
        partyName.setWrapText(true);
        partyName.setMaxWidth(200); // adjust based on design
        partyName.setTextAlignment(TextAlignment.LEFT);
        partyName.setAlignment(Pos.CENTER_LEFT);
        partyName.getStyleClass().add("selected-party-info-text");

        Label location = new Label(p.getLocation());
        location.getStyleClass().add("selected-party-info-text");



        VBox textBox = new VBox(5, partyName, location);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(15,time, textBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(260);
        card.setMinWidth(260);
        card.getStyleClass().add("card");
        card.getStyleClass().add("selected-party-card");
        card.setUserData(p.getPartyId());
        card.setOnMouseClicked(event -> {
            System.out.println(p.getPartyId());

            Long id = (Long)card.getUserData();
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
        VBox.setMargin(card, new Insets(10, 0, 0, 0));
        return card;
    }

    private HBox createPastPartiesCard(Party p){
        Label day = new Label(formatDay(p.getPartyDate()));
        day.getStyleClass().add("past-month-text");
        day.setTextAlignment(TextAlignment.CENTER);
        day.setAlignment(Pos.CENTER);
        System.out.println(day.getStyleClass());
        Label month = new Label(formatMonth(p.getPartyDate()));
        month.getStyleClass().add("past-month-label");
        month.setTextAlignment(TextAlignment.CENTER);
        month.setAlignment(Pos.CENTER);
        day.setPadding(Insets.EMPTY);
        month.setPadding(Insets.EMPTY);
        System.out.println(month.getStyleClass());
        VBox dateContainer = new VBox(month, day);
        day.maxWidthProperty().bind(dateContainer.widthProperty());
        month.maxWidthProperty().bind(dateContainer.widthProperty());
        dateContainer.setAlignment(Pos.CENTER);
        dateContainer.setMaxWidth(75);
        dateContainer.setMinWidth(75);
        dateContainer.setSpacing(0);


        Label partyName = new Label(p.getPartyName());
        partyName.setWrapText(true);
        partyName.setMaxWidth(250); // adjust based on design
        partyName.setTextAlignment(TextAlignment.LEFT);
        partyName.setAlignment(Pos.CENTER_LEFT);
        partyName.getStyleClass().add("past-party-name-text");

        Image clockImg = new Image(getClass().getResource("images/clock.png").toExternalForm());
        Image houseImg = new Image(getClass().getResource("images/house.png").toExternalForm());
        ImageView clock = new ImageView(clockImg);
        ImageView house = new ImageView(houseImg);
        double size = 10;
        clock.setFitWidth(size);
        clock.setFitHeight(size);
        house.setFitWidth(size);
        house.setFitHeight(size);
        Label time = new Label(formatTime(p.getPartyDate()));
        Label location = new Label(p.getLocation());
        time.getStyleClass().add("past-party-info-text");
        location.getStyleClass().add("past-party-info-text");

        HBox timeContainer = new HBox(5, clock, time);
        HBox locationContainer = new HBox(5, house, location);
        timeContainer.setAlignment(Pos.CENTER_LEFT);
        locationContainer.setAlignment(Pos.CENTER_LEFT);


        VBox textBox = new VBox(5, partyName, timeContainer, locationContainer);
        textBox.setAlignment(Pos.CENTER_LEFT);

        HBox card = new HBox(10,dateContainer, textBox);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMaxWidth(260);
        card.setMinWidth(260);
        card.getStyleClass().add("card");
        card.getStyleClass().add("select-card");
        card.setUserData(p.getPartyId());
        card.setOnMouseClicked(event -> {
            System.out.println(p.getPartyId());

            Long id = (Long)card.getUserData();
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
        VBox.setMargin(card, new Insets(10, 0, 0, 0));
        return card;
    }
    @FXML
    void addParty(ActionEvent event) {

    }






}
