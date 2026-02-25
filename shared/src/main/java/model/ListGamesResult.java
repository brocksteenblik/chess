package model;

import java.util.Collection;

public record ListGamesResult(Collection<GameData> gameData) {
    @Override
    public String toString() {
        return "games: [" +
                gameData +
                ']';
    }
}
