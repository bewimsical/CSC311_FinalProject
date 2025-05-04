package edu.farmingdale.csc311_finalproject;

public class SearchGame {
    private int id;
    private String name;
    private String year;

    public SearchGame() {
    }

    public SearchGame(int id, String name, String year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setName(String name) {
        this.name = name;
    }



    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return name + " (" + year + ")";
    }
}
