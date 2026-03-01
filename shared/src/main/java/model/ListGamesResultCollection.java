package model;

import java.util.Collection;

public record ListGamesResultCollection(Collection<ListGamesResult> games) {
    @Override
    public String toString() {
        return "ListGamesResultCollection{" +
                "gamesList=" + games +
                '}';
    }
}
