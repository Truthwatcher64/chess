package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public void clear() throws DataAccessException;
    public void addAuthToken(AuthData data) throws DataAccessException;
    public void deleteAuthToken(String authToken) throws DataAccessException;
    public String getUsername(String authToken) throws DataAccessException;
    public String getAuthToken(String username) throws DataAccessException;

}
