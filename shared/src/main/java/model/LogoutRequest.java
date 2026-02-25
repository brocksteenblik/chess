package model;

public record LogoutRequest(String authToken) {
    @Override
    public String toString() {
        return "LogoutRequest{" +
                "authToken:'" + authToken + '\'' +
                '}';
    }
}
