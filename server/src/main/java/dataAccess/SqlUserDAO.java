package dataAccess;

import model.UserData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlUserDAO implements UserDAO{
    private final String TABLENAME ="user";
    @Override
    public void clear() throws DataAccessException {
        DatabaseManager databaseManager= new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE " + TABLENAME)) {
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DataAccessException("Problem clearing "+TABLENAME+": "+e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        DatabaseManager databaseManager=new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()) {
            var preparedStatement = conn.createStatement();
            ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM " + TABLENAME + " where `username`=\"" + username + "\";");
            while(resultSet.next()){
                String usernameNew=resultSet.getString("username");
                String passwordNew=resultSet.getString("password");
                String emailNew=resultSet.getString("email");
                return new UserData(usernameNew, passwordNew, emailNew);
            }
            return null;

        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addUser(UserData newUser) throws DataAccessException {
        DatabaseManager databaseManager=new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO "+TABLENAME+" (username, password, email) VALUES(?, ?, ?);")) {
                preparedStatement.setString(1, newUser.username());
                preparedStatement.setString(2, newUser.password());
                preparedStatement.setString(3, newUser.email());
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String getPassword(String username) throws DataAccessException {
        DatabaseManager databaseManager=new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()) {
            var preparedStatement = conn.createStatement();
            ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM " + TABLENAME + " where `username`=\"" + username + "\";");
            while(resultSet.next()){
                return resultSet.getString("password");
            }
            return null;

        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
