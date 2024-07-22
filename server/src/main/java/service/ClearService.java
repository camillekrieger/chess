package service;

import dataaccess.*;

public class ClearService {
    public void clear() throws DataAccessException {
        AuthDAO mad = new MemoryAuthDAO();
        GameDAO mgd = new MemoryGameDAO();
        UserDAO mud = new MemoryUserDAO();
        mad.clear();
        mgd.clear();
        mud.clear();
    }
}
