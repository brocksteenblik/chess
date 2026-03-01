package model;

import java.util.Collection;

public record ListGamesResult(int gameID, String whiteUsername, String blackUsername, String gameName) {
    @Override
    public String toString() {
        return "games{" +
                "gameID:" + gameID +
                ", whiteUsername:'" + whiteUsername + '\'' +
                ", blackUsername:'" + blackUsername + '\'' +
                ", gameName:'" + gameName + '\'' +
                '}';
    }
}
