package dataAccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    private FakeData dao;
    public MemoryAuthDAO(FakeData dao){
        this.dao=dao;
    }


    @Override
    public void clear() {
        dao.getAuths().clear();
    }

    @Override
    public void addAuthToken(AuthData data) throws DataAccessException {

    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {

    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public String getAuthToken(String username) throws DataAccessException {
        return null;
    }


}
