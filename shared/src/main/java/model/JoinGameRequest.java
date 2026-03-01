package model;

public record JoinGameRequest(PlayerColor playerColor, int gameID) {
    public enum PlayerColor{
        WHITE,
        BLACK
    }

    @Override
    public String toString() {
        return "JoinGameRequest{" +
                "playerColor:" + playerColor +
                ", gameID:" + gameID +
                '}';
    }
}
