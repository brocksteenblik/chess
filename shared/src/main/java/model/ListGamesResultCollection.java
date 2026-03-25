package model;

import java.util.ArrayList;
import java.util.Collection;

public record ListGamesResultCollection(ArrayList<ListGamesResult> games) {
    @Override
    public String toString() {
        return "ListGamesResultCollection{" +
                "gamesList=" + games +
                '}';
    }
}
