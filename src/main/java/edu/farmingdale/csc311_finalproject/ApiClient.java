package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


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
            return mapper.readValue(response.toString(), typeReference);
        } else {
            System.out.println("GET request did not work.");
            return null;
        }
    }
    //TODO update for requests with no return?
//    public static <T, R> R sendPOST(String POST_URL, T requestBody, TypeReference<R> responseType) throws IOException {
//        URL url = new URL(POST_URL);
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);
//        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("Accept", "application/json");
//        con.setDoOutput(true);
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        // Serialize and write the request body
//        try (OutputStream os = con.getOutputStream()) {
//            byte[] input = mapper.writeValueAsBytes(requestBody);
//            os.write(input);
//            os.flush();
//        }
//
//        int responseCode = con.getResponseCode();
//        System.out.println("POST Response Code :: " + responseCode);
//
//        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
//            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
//                StringBuilder response = new StringBuilder();
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                return mapper.readValue(response.toString(), responseType);
//            }
//        } else {
//            System.out.println("POST request failed.");
//            return null;
//        }
//    }
    //TODO check this method
    public static <T, R> R sendPOST(String POST_URL, T requestBody, TypeReference<R> responseType) throws IOException {
        URL url = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        ObjectMapper mapper = new ObjectMapper();

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

    /**
     * GET Request
     * @param gameId
     * @return
     */
    public static String searchGameUrl(int gameId){
        return String.format("%s/search/%s",BASE_API_URL,gameId);
    }

    /**
     * POST
     * @param gameName
     * @return
     */
    public static String searchGameUrl(String gameName){
        return String.format("%s/search/%s",BASE_API_URL,gameName);
    }

    /**
     * GET
     * @param userId
     * @return
     */
    public static String getUserUrl(int userId) {
        return String.format("%s/users/%d", BASE_API_URL, userId);
    }

    /**
     * Post
     * @return
     */
    public static String createUserUrl() {
        return String.format("%s/users/create", BASE_API_URL);
    }

    /**
     * POST
     * @param userId
     * @param gameId
     * @return
     */
    public static String addGameToUser(int userId, int gameId){
        return String.format("%s/users/addgame?user=%d&game=%d", BASE_API_URL,userId,gameId);
    }
    public static String addFriendToUser(int userId, int friendId){
        return String.format("%s/users/addfriend?user=%d&friend=%d", BASE_API_URL,userId,friendId);
    }

    /**
     * GET
     * @param userId
     * @return
     */
    public static String getUserGames(int userId){
        return String.format("%s/users/%d/games", BASE_API_URL,userId);
    }
    public static String getUserFriends(int userId){
        return String.format("%s/users/%d/friends", BASE_API_URL,userId);
    }
    public static String getUserParties(int userId){
        return String.format("%s/users/%d/parties", BASE_API_URL,userId);
    }
    public static String getUserHostedParties(int userId){
        return String.format("%s/users/%d/hosted", BASE_API_URL,userId);
    }
    public static String deleteGameFromUser(int userId, int gameId){
        return String.format("%s/users/deletegame?user=%d&game=%d", BASE_API_URL,userId,gameId);
    }
    public static String deleteFriendFromUser(int userId, int friendId){
        return String.format("%s/users/deletegame?user=%d&friend=%d", BASE_API_URL,userId,friendId);
    }



}

