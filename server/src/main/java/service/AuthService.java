package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class AuthService {
    public void clear() throws DataAccessException {
        MemoryAuthDAO mad = new MemoryAuthDAO();
        MemoryGameDAO mgd = new MemoryGameDAO();
        MemoryUserDAO mud = new MemoryUserDAO();
        mad.clear();
        mgd.clear();
        mud.clear();
    }
}
