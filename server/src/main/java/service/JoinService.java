package service;

import request.JoinRequest;
import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import server.ServerException;

public class JoinService extends Service{
    public void join(JoinRequest request, String authToken) throws ServerException {

        try{
            SqlAuthDAO authDAO=new SqlAuthDAO();
            SqlGameDAO gameDAO=new SqlGameDAO();
            checkAuthorization(authToken);

            request.setUserName(authDAO.getUsername(authToken));
            //Check gameID
            if(gameDAO.getGame(request.getGameID())==null){
                throw new ServerException("Error: Invalid gameID", 400);
            }

            //Checking team color
            ChessGame.TeamColor color;
            if(request.getPlayerColor()==null || request.getPlayerColor().isBlank()){
                throw new ServerException("Error: Bad Request", 400);
            }
            if(request.getPlayerColor().equalsIgnoreCase("White")){
                color= ChessGame.TeamColor.WHITE;
            }
            else if(request.getPlayerColor().equalsIgnoreCase("Black")){
                color= ChessGame.TeamColor.BLACK;
            }
            else{
                throw new ServerException("Error: Incorrect Team color", 400);
            }

            if(color == ChessGame.TeamColor.WHITE) {
                String temp=gameDAO.getGame(request.getGameID()).whiteUsername();
                if (temp==null || gameDAO.getGame(request.getGameID()).whiteUsername().equals(request.getUserName())) {
                        gameDAO.addPlayer(request.getUserName(), color, request.getGameID());
                }

                else{
                    throw new ServerException("Error: Already taken", 403);
                }
            }
            if(color == ChessGame.TeamColor.BLACK) {
                if (gameDAO.getGame(request.getGameID()).blackUsername()==null || gameDAO.getGame(request.getGameID()).blackUsername().equals(request.getUserName())){
                    gameDAO.addPlayer(request.getUserName(), color, request.getGameID());
                }
                else{
                    throw new ServerException("Error: Already taken", 403);
                }
            }


        }
        catch (DataAccessException e){
            throw new ServerException(e.getMessage(), 500);
        }
    }

}
