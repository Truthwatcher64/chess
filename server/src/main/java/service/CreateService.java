package service;


import Request.CreateRequest;
import Result.CreateResult;
import dataaccess.*;
import model.GameData;
import server.ServerException;

import java.util.HashSet;
import java.util.Set;

/**
 * Service that creates a game in the database
 */
public class CreateService extends Service{
    /**
     * Attempts to create a new game
     * @param request of the data for the new game
     * @return result message if there is an error or blank result if successful
     */
    public CreateResult create(CreateRequest request) throws ServerException {
        CreateResult result=new CreateResult();
        try {
            checkAuthorization(request.getAuthToken());
            SqlGameDAO gameDAO = new SqlGameDAO();
            Set<GameData> list=(HashSet<GameData>)gameDAO.getAllGames();
            for(GameData game : list){
                if(game.gameName()!=null && game.gameName().equals(request.getGameName())) {
                    throw new ServerException("Error: Already taken", 403);
                }
            }

            int gameID=gameDAO.addGame(request.getGameName());
            if(gameID==0){
                result=new CreateResult();
                throw new DataAccessException("Error: Bad Request");
            }
            result.setGameID(gameID);
        }
        catch(DataAccessException e){
            result=new CreateResult();
            throw new ServerException(e.getMessage(), 400);
        }

        return result;
    }
}
