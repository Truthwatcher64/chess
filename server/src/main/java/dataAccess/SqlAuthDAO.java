package dataAccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO {

    private final String TABLENAME="auth";

    @Override
    public void clear() throws DataAccessException{
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
    public void addAuthToken(AuthData data) throws DataAccessException {
        DatabaseManager databaseManager=new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO "+TABLENAME+" (authToken, username) VALUES(?, ?);")) {
                preparedStatement.setString(2, data.username());
                preparedStatement.setString(1, data.authToken());
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException {
        DatabaseManager databaseManager=new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM "+TABLENAME+" WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }
        catch(Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        DatabaseManager databaseManager=new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()) {
            var preparedStatement = conn.createStatement();
            ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM " + TABLENAME + " where `authToken`=\"" + authToken + "\";");
            while(resultSet.next()){
                return resultSet.getString("username");
            }
            return null;

        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    @Override
    public String getAuthToken(String username) throws DataAccessException {
        DatabaseManager databaseManager=new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()) {
            var preparedStatement = conn.createStatement();
            ResultSet resultSet = preparedStatement.executeQuery("SELECT * FROM " + TABLENAME + " where `authToken`=\"" + username + "\";");
            while(resultSet.next()){
                return resultSet.getString("authToken");
            }
            return null;

        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
