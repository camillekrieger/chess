package model;

public class AuthData {
    String authToken;
    String username;

    public AuthData(String username, String token){
        this.username = username;
        this.authToken = token;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getAuthFromUser(String username){
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
