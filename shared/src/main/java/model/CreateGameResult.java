package model;

public record CreateGameResult(int GameID) {
    @Override
    public String toString() {
        return "CreateGameResult{" +
                "GameID:'" + GameID + '\'' +
                '}';
    }
}
