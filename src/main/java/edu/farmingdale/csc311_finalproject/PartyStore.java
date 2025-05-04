package edu.farmingdale.csc311_finalproject;

import java.util.ArrayList;
import java.util.List;

public class PartyStore {
    private static final List<Party> parties = new ArrayList<>();

    public static void addParty(Party party) {
        parties.add(party);
    }

    public static List<Party> getAllParties() {
        return new ArrayList<>(parties);
    }

    public static void clear() {
        parties.clear();
    }
}
