package edu.farmingdale.csc311_finalproject;

import java.time.LocalDateTime;

public class Party {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;
    private String partyName;
    private LocalDateTime partyDate;
    private String location;

    public Party() {
    }

    public Party(String partyName, LocalDateTime partyDate, String location) {
        this.partyName = partyName;
        this.partyDate = partyDate;
        this.location = location;
    }

    public Party(Long id, String partyName, LocalDateTime partyDate, String location) {
        this.id = id;
        this.partyName = partyName;
        this.partyDate = partyDate;
        this.location = location;
    }


    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public LocalDateTime getPartyDate() {
        return partyDate;
    }

    public void setPartyDate(LocalDateTime partyDate) {
        this.partyDate = partyDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
