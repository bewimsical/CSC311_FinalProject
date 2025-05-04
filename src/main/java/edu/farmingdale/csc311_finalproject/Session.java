package edu.farmingdale.csc311_finalproject;

public class Session {
    private static Session instance;
    private User currentUser;


    private Session() {
        //private constructor to enforce singleton
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return this.currentUser;
    }

    public void clearSession() {
        currentUser = null;
        instance = null;
    }

    private PartyController partyController;
    public void setPartyController(PartyController controller) {
        this.partyController = controller;
    }
    public PartyController getPartyController() {
        return partyController;
    }



}

