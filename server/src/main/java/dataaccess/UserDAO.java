package dataaccess;

import model.UserData;

public interface UserDAO {

    public default void createUser(String username, String password, String email){
        new UserData(username, password, email);
    }

    pubilc UserData getUser(String username){
        //return userdata
    }
}
