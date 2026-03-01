package model;

import chess.ChessGame;
import com.google.gson.Gson;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    @Override
    public String toString() {
        return new Gson().toJson("games{" +
                "gameID:" + gameID +
                ", whiteUsername:'" + whiteUsername + '\'' +
                ", blackUsername:'" + blackUsername + '\'' +
                ", gameName:'" + gameName + '\'' +
                '}');
    }

    public GameData setWhiteUsername(String username){return new GameData(gameID, username, blackUsername, gameName, game);}

    public GameData setBlackUsername(String username){return new GameData(gameID, whiteUsername, username, gameName, game);}
}
