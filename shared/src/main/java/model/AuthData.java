package model;

public class AuthData {
    String authToken;
    String username;

    public AuthData(String username){
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
