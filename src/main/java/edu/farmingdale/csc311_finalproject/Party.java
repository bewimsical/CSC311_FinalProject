package edu.farmingdale.csc311_finalproject;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Party {
    private Long partyId;

    @JsonProperty("party_name")
    private String partyName;
    @JsonProperty("party_date")
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
        this.partyId = id;
        this.partyName = partyName;
        this.partyDate = partyDate;
        this.location = location;
    }


    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
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

    @Override
    public String toString() {
        return "Party{" +
                "id=" + partyId +
                ", partyName='" + partyName + '\'' +
                ", partyDate=" + partyDate +
                ", location='" + location + '\'' +
                '}';
    }
}
