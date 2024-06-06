package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SqlGameDAO implements GameDAO{
    private static final String TABLENAME="game";
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
    public int addGame(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        String gameJ = new Gson().toJson(game);
        DatabaseManager databaseManager= new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("INSERT INTO "+TABLENAME+" (name, gameJSON) VALUES(? , ?);")){
                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, gameJ);
                preparedStatement.executeUpdate();
            }
            catch (SQLException e){
                throw new DataAccessException("Error: Problem with SQL: "+e.getMessage());
            }
            try(var preparedStatement = conn.prepareStatement("SELECT `gameID` FROM "+TABLENAME+" WHERE `name`=\""+gameName+"\";")){
                ResultSet results=preparedStatement.executeQuery();
                while(results.next()){
                    return results.getInt("gameID");
                }
            }
            catch (SQLException e){
                throw new DataAccessException("Error: Problem with SQL: "+e.getMessage());
            }
        }
        catch (SQLException e){
            throw new DataAccessException("Error: Problem with SQL: "+e.getMessage());
        }
        return 0;
    }

    @Override
    public Collection<GameData> getAllGames() throws DataAccessException {
        Set<GameData> gameList=new HashSet<>();
        int gameID=0;
        String gameName=null;
        String whiteUsername=null;
        String blackUsername=null;
        String json=null;
        ChessGame game=null;
        DatabaseManager databaseManager= new DatabaseManager();
        try(Connection conn=databaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM "+TABLENAME+";")){
                ResultSet results=preparedStatement.executeQuery();
                while(results.next()){
                    gameID=results.getInt("gameID");
                    gameName=results.getString("name");
                    whiteUsername=results.getString("whiteUsername");
                    blackUsername=results.getString("blackUsername");
                    json=results.getString("gameJSON");
                    game=new Gson().fromJson(json, ChessGame.class);
                    gameList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                }
                return gameList;
            }
        }
        catch (SQLException e){
            throw new DataAccessException("Error: Problem with SQL: "+e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        DatabaseManager databaseManager=new DatabaseManager();
        int gameIDNew=0;
        String gameName=null;
        String whiteUsername=null;
        String blackUsername=null;
        String json=null;
        ChessGame game=null;
        try(Connection conn = databaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM "+TABLENAME+" WHERE `gameID`="+gameID+";")){
                ResultSet resultSet=preparedStatement.executeQuery();
                while(resultSet.next()){
                    gameIDNew=resultSet.getInt("gameID");
                    gameName=resultSet.getString("name");
                    whiteUsername=resultSet.getString("whiteUsername");
                    blackUsername=resultSet.getString("blackUsername");
                    json=resultSet.getString("gameJSON");
                    game=new Gson().fromJson(json, ChessGame.class);
                    return new GameData(gameIDNew, whiteUsername, blackUsername, gameName, game);
                }
                return null;
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addPlayer(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException {
        DatabaseManager databaseManager=new DatabaseManager();
        try(Connection conn = databaseManager.getConnection()){
            if(color== ChessGame.TeamColor.WHITE) {
                try (var preparedStatement = conn.prepareStatement("UPDATE " + TABLENAME + " SET `whiteUsername`=\""+username+"\" WHERE `gameID`="+gameID+";")) {
                    preparedStatement.executeUpdate();
                }
            }
            else{
                try (var preparedStatement = conn.prepareStatement("UPDATE " + TABLENAME + " SET `blackUsername`=\""+username+"\" WHERE `gameID`="+gameID+";")) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e){
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void removePlayer(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException {

    }

    @Override
    public void updateGame(String gameJson, int gameID) throws DataAccessException {

    }
}
