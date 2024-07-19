package dataaccess;

import model.AuthData;

import java.util.UUID;

public interface AuthDAO {

    public default void createAuth(String user){
        String token = UUID.randomUUID().toString();
        new AuthData(user, token);
    }

    public AuthData getAuth(String token){
        //get authData
    }

    public default void deleteAuth(String token){
        //delete the thing
    }
}
