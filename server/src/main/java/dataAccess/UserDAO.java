package dataAccess;

import model.UserData;

public interface UserDAO {
    public void clear() throws DataAccessException;
    public UserData getUser(String username) throws DataAccessException;
    public void addUser(UserData newUser) throws DataAccessException;
    public String getPassword(String username) throws DataAccessException;
}
