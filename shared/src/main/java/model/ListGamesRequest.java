package model;

public record ListGamesRequest(String authToken) {
    @Override
    public String toString() {
        return "ListGamesRequest{" +
                "authToken:'" + authToken + '\'' +
                '}';
    }
}
