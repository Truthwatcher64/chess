package dataAccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    private FakeData dao;
    public MemoryUserDAO(FakeData dao){
        this.dao=dao;
    }

    @Override
    public void clear() throws DataAccessException {
        this.dao.getUsers().clear();
    }

    public UserData getUser(String username) throws DataAccessException{
        for(UserData user: dao.getUsers()){
            if(user.username().equals(username)){
                return user;
            }
        }
        return null;

    }

    public void addUser(UserData newUser) throws DataAccessException{
        dao.getUsers().add(newUser);

    }

    @Override
    public String getPassword(String username) throws DataAccessException {
        return null;
    }

}
