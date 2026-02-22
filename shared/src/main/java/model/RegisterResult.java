package model;

public record RegisterResult(String username, String authToken) {
    @Override
    public String toString() {
        return "RegisterResult{" +
                "username:'" + username + '\'' +
                ", authToken:'" + authToken + '\'' +
                '}';
    }
}
