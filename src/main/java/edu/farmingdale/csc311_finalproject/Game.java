package edu.farmingdale.csc311_finalproject;

public class Game {

    private int gameId;
    private int bggId;
    private String game_name;
    private int minPlayers;
    private int maxPlayers;
    private int playTime;
    private String imgUrl;
    private String category;

    // Default Constructor
    public Game() {
    }

    public Game(int gameId, int bggId, String game_name, int minPlayers, int maxPlayers, int playTime, String imgUrl, String category) {
        this.gameId = gameId;
        this.bggId = bggId;
        this.game_name = game_name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playTime = playTime;
        this.imgUrl = imgUrl;
        this.category = category;
    }
    public Game(int bggId, String game_name, int minPlayers, int maxPlayers, int playTime, String imgUrl, String category) {
        this.bggId = bggId;
        this.game_name = game_name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playTime = playTime;
        this.imgUrl = imgUrl;
        this.category = category;
    }

    // Getters and Setters
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int objectId) {
        this.gameId = objectId;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String name) {
        this.game_name = name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getPlayTime() {
        return playTime;
    }
    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // toString Method
    @Override
    public String toString() {
        return "Game{" +
                "objectId=" + gameId +
                ", name='" + game_name + '\'' +
                ", minPlayers='" + minPlayers + '\'' +
                ", maxPlayers='" + maxPlayers + '\'' +
                ", playTime='" + playTime + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public int getBggId() {
        return bggId;
    }

    public void setBggId(int bggId) {
        this.bggId = bggId;
    }

}

