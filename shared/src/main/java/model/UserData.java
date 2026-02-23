package model;

public record UserData(String username, String password, String email) {
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username:'" + username + '\'' +
                ", password:'" + password + '\'' +
                ", email:'" + email + '\'' +
                '}';
    }
}
