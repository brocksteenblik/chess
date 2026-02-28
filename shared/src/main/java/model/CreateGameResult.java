package model;

public record CreateGameResult(int gameID) {
    @Override
    public String toString() {
        return "CreateGameResult{" +
                "GameID:'" + gameID + '\'' +
                '}';
    }
}
