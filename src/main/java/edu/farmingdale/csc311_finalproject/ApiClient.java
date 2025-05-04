package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;


public final class ApiClient {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String BASE_API_URL = "http://localhost:8080";


    private ApiClient(){}

    public static <T> T sendGET(String GET_URL, TypeReference<T> typeReference) throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.readValue(response.toString(), typeReference);
        } else {
            System.out.println("GET request did not work.");
            return null;
        }
    }
    //TODO check this method
    public static <T, R> R sendPOST(String POST_URL, T requestBody, TypeReference<R> responseType) throws IOException {
        URL url = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Only send body if not null
        if (requestBody != null) {
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = mapper.writeValueAsBytes(requestBody);
                os.write(input);
                os.flush();
            }
        }

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        // Handle success or no-content response
        if (responseCode == HttpURLConnection.HTTP_OK ||
                responseCode == HttpURLConnection.HTTP_CREATED ||
                responseCode == HttpURLConnection.HTTP_NO_CONTENT) {

            InputStream inputStream = con.getInputStream();
            if (inputStream == null || inputStream.available() == 0 || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                return null;
            }

            try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                if (response.toString().isBlank()) {
                    return null;
                }
                return mapper.readValue(response.toString(), responseType);
            }
        } else {
            System.out.println("POST request failed with response code: " + responseCode);
            return null;
        }
    }

    public static boolean sendDELETE(String DELETE_URL) throws IOException {
        URL url = new URL(DELETE_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept", "application/json");

        int responseCode = con.getResponseCode();
        System.out.println("DELETE Response Code :: " + responseCode);

        return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT;
    }

    //Methods for generating the URLs.
    //for GET and POST: TypeReference<R> responseType param is: new TypeReference<[return type]>() {}
    //for POST T requestBody is an object

    //===========GAME URLS===========//

    //GET Request returns a game object
    public static String getGameUrl(int gameId){
        return String.format("%s/games/%d",BASE_API_URL,gameId);
    }

    //GET returns a list of search game objects
    public static String searchGameUrl(String gameName){
        return String.format("%s/search/%s",BASE_API_URL,gameName);
    }

    //===========USER URLS===========//

    //GET - returns user object
    public static String getUserUrl(long userId) {
        return String.format("%s/users/%d", BASE_API_URL, userId);
    }

    //POST - request body is a User object, returns user object
    public static String createUserUrl() {
        return String.format("%s/users/create", BASE_API_URL);
    }

    //POST request body is null, returns null
    public static String addGameToUser(long userId, int gameId){
        return String.format("%s/users/addgame?user=%d&game=%d", BASE_API_URL,userId,gameId);
    }

    //POST request body is null, returns null
    public static String addFriendToUser(long userId, int friendId){
        return String.format("%s/users/addfriend?user=%d&friend=%d", BASE_API_URL,userId,friendId);
    }

    //GET returns a set of game objects
    public static String getUserGames(long userId){
        return String.format("%s/users/%d/games", BASE_API_URL,userId);
    }

    //GET returns a set of user objects
    public static String getUserFriends(long userId){
        return String.format("%s/users/%d/friends", BASE_API_URL,userId);
    }

    //GET returns a set of party objects - NOTE: this endpoint is under construction
    public static String getUserParties(long userId){
        return String.format("%s/users/%d/parties", BASE_API_URL,userId);
    }

    //GET returns a set of game objects - NOTE: this endpoint is under construction
    public static String getUserHostedParties(long userId){
        return String.format("%s/users/%d/hosted", BASE_API_URL,userId);
    }

    //DELETE returns nothing
    public static String deleteGameFromUser(long userId, int gameId){
        return String.format("%s/users/deletegame?user=%d&game=%d", BASE_API_URL,userId,gameId);
    }
    //DELETE returns nothing
    public static String deleteFriendFromUser(long userId, long friendId){
        return String.format("%s/users/deletefriend?user=%d&friend=%d", BASE_API_URL,userId,friendId);
    }

    // GET - Returns a list of all users
    public static String getAllUsersUrl() {
        return String.format("%s/users", BASE_API_URL);
    }

    // POST - Adds a friend to the user's friend list; request body is null, returns void
    public static String addFriendUrl(long userId, long friendId) {
        return String.format("%s/users/addfriend?user=%d&friend=%d", BASE_API_URL, userId, friendId);
    }


    //===========PARTY URLS===========//

    //POST request body is a Party object, returns a party object
    public static String createParty(long userId){
        return String.format("%s/parties/create/%d", BASE_API_URL,userId);
    }
    //GET  returns a party object
    public static String getParty(long partyId){
        return String.format("%s/parties/%d", BASE_API_URL,partyId);
    }
    //DELETE
    public static String deleteParty(long partyId){
        return String.format("%s/parties/delete/%d", BASE_API_URL,partyId);
    }
    //GET  returns a list of user objects
    public static String getPartyUsers(long partyId){
        return String.format("%s/parties/%d/users", BASE_API_URL,partyId);
    }
    //GET  returns a list of user objects
    public static String getPartyHosts(long partyId){
        return String.format("%s/parties/%d/hosts", BASE_API_URL,partyId);
    }
    //POST  request body is null, returns null
    public static String addUserToParty(long userId, long partyId, boolean isHost){
        return String.format("%s/parties/adduser?user=%d&party=%d&host=%b", BASE_API_URL,userId,partyId,isHost);
    }
    //POST  request body is null, returns null
    public static String editUserInParty(long userId, long partyId, boolean isHost){
        return String.format("%s/parties/edit/user?user=%d&party=%d&host=%b", BASE_API_URL,userId,partyId,isHost);
    }
    //DELETE
    public static String deleteUserFromParty(long userId, long partyId){
        return String.format("%s/parties/delete/user?user=%d&party=%d", BASE_API_URL,userId,partyId);
    }
    //GET returns a list of user objects
    public static String getSelectedGames(Long partyId){
        return String.format("%s/parties/%d/games", BASE_API_URL, partyId);
    }
    //GET returns a list of user objects
    public static String getUserSelectedGames(Long partyId, Long userId){
        return String.format("%s/parties/%d/games/selected/%d", BASE_API_URL, partyId, userId);
    }
    //POST request body is null, returns void
    public static String selectGame(Long partyId, int gameId, Long userId){
        return String.format("%s/parties/%d/games/%d/%d", BASE_API_URL, partyId, gameId, userId);
    }
    //DELETE request body is null, returns void
    public static String deselectGame(Long partyId, int gameId, Long userId){
        return String.format("%s/parties/%d/games/%d/%d", BASE_API_URL, partyId, gameId, userId);
    }



    //=============EXAMPLE USAGE=============//
    public static void main(String[] args) throws IOException {
//        User hazel = sendGET(getUserUrl(1), new TypeReference<User>() {});
//        System.out.println(hazel.toString());
//
//        List<SearchGame> games = sendGET(searchGameUrl("clue"), new TypeReference<List<SearchGame>>() {});
//        for(SearchGame game: games){
//            System.out.println(game.toString());
//        }
//
//        System.out.println(sendGET(getGameUrl(13), new TypeReference<Game>() {}));
//
//        sendPOST(createUserUrl(),
//                new User("TeddySpaghetti", "Teddy", "Spaghetti", "spaghetticat@gmail.com", "/images/wizard_cat.PNG", "snacksplz"),
//                new TypeReference<User>() {
//                });
//
//        sendPOST(addGameToUser(1, 5), null, new TypeReference<Void>() {
//        });
//        sendPOST(addFriendToUser(1, 2), null, new TypeReference<Void>() {
//        });
//
//        set up an observable list in the profile page
//        private final ObservableList<Game> gameData = FXCollections.observableArrayList();
//        //in initialize-
//        List<Game> games =sendGET(getUserGames(1), new TypeReference<List<Game>>() {
//        });
//        gameData.addAll(games);
//        for (Game g : games){
//            System.out.println(g.toString());
//        }
//
//        List<User> friends =sendGET(getUserFriends(1), new TypeReference<List<User>>() {
//        });
//        for (User f : friends){
//            System.out.println(f.toString());
//        }
//
//        List<Party> parties =sendGET(getUserHostedParties(1), new TypeReference<List<Party>>() {
//        });
//        for (Party g : parties){
//            System.out.println(g.toString());
//        }
//        sendDELETE(deleteGameFromUser(1,2));
//        sendDELETE(deleteFriendFromUser(1,2));
//        sendPOST(createParty(2),
//                new Party("Salmon Party", LocalDateTime.now(), "Teddy's House"),
//                new TypeReference<Party>() {
//                });
//        System.out.println(sendGET(getParty(2), new TypeReference<Party>() {}));
//        List<User> users = sendGET(getPartyUsers(1), new TypeReference<List<User>>() {
//        });
//        for(User u:users){
//            System.out.println(u.toString());
//        }
//        List<User> users = sendGET(getPartyHosts(1), new TypeReference<List<User>>() {
//        });
//        for(User u:users){
//            System.out.println(u.toString());
//        }
//        sendPOST(addUserToParty(1, 2, true), null, new TypeReference<Void>() {});
//        sendPOST(editUserInParty(1, 2, false), null, new TypeReference<Void>() {});
//        sendDELETE(deleteUserFromParty(1,2));




    }






}

