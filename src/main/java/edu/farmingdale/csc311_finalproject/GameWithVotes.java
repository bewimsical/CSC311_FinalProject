package edu.farmingdale.csc311_finalproject;

public class GameWithVotes {


    private int gameId;
    private String game_name;
    private int minPlayers;
    private int maxPlayers;
    private int playTime;
    private String imgUrl;
    private int votes;

    // Default Constructor
    public GameWithVotes() {
    }

    public GameWithVotes(int gameId, String game_name, int minPlayers, int maxPlayers, int playTime, String imgUrl, int votes) {
        this.gameId = gameId;
        this.game_name = game_name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playTime = playTime;
        this.imgUrl = imgUrl;
        this.votes = votes;
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
                '}';
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
