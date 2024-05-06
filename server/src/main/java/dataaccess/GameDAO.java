package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public void clear() throws DataAccessException;
    public int addGame(String newGame) throws DataAccessException;
    public Collection<GameData> getAllGames() throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public void addPlayer(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException;
    public void removePlayer(String username, ChessGame.TeamColor color, int gameID) throws DataAccessException;
    public void updateGame(String gameJson, int gameID) throws DataAccessException;
}
