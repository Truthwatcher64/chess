package service;

import Request.LoginRequest;
import Result.LoginResult;
import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import dataAccess.SqlUserDAO;
import model.AuthData;
import server.ServerException;

import java.util.UUID;

public class LoginService {

    public LoginResult login(LoginRequest request) throws ServerException{
        SqlUserDAO userDAO=new SqlUserDAO();
        SqlAuthDAO authDAO=new SqlAuthDAO();
        LoginResult result=new LoginResult();

        if (request.getUsername() == null || request.getPassword() == null) {
            throw new ServerException("Error: Unauthorized, Check your username and password", 401);
        }
        else if (request.getUsername().isBlank() || request.getPassword().isBlank()) {
            throw new ServerException("Error: Unauthorized, Check your username and password", 401);
        }

        try{
            //FIXME add bycrypt here
            String dbPassword=userDAO.getPassword(request.getUsername());
            String inputPassword=request.getPassword();
            if(dbPassword==null || !dbPassword.equals(inputPassword)){
                throw new ServerException("Error: Unauthorized, Check your username and password", 401);
            }
            String tempAuthToken = UUID.randomUUID().toString();
            AuthData authToken = new AuthData(tempAuthToken, request.getUsername());
            if(authDAO.getAuthToken(request.getUsername())==null) {
                authDAO.addAuthToken(authToken);
            }
            else{
                authDAO.deleteAuthToken(authDAO.getAuthToken(request.getUsername()));
                authDAO.addAuthToken(authToken);
            }
            result.setUsername(request.getUsername());
            result.setAuthToken(tempAuthToken);
            return result;

        }
        catch (DataAccessException e){
            throw new ServerException(e.getMessage(), 401);
        }
    }
}
