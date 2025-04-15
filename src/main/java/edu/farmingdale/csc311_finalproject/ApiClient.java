package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    public static <T, R> R sendPOST(String POST_URL, T requestBody, TypeReference<R> responseType) throws IOException {
        URL url = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        ObjectMapper mapper = new ObjectMapper();

        // Serialize and write the request body
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = mapper.writeValueAsBytes(requestBody);
            os.write(input);
            os.flush();
        }

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return mapper.readValue(response.toString(), responseType);
            }
        } else {
            System.out.println("POST request failed.");
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

    public static String getUserUrl(int userId) {
        return String.format("%s/users/%d", BASE_API_URL, userId);
    }
    public static String createUserUrl() {
        return String.format("%s/users/create", BASE_API_URL);
    }

}

