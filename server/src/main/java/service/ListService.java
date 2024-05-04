package service;

import java.util.Collection;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.SqlGameDAO;
import model.GameData;
import server.ServerException;

public class ListService extends Service{
    public Collection<GameData> listGames(String authToken) throws ServerException {
        Collection<GameData> list=null;
        try{
            SqlGameDAO gameDAO=new SqlGameDAO();
            checkAuthorization(authToken);
            list=gameDAO.getAllGames();
            return list;
        }
        catch (DataAccessException e){
            throw new ServerException(e.getMessage(), 500);
        }
    }
}
