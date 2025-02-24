package service;

import request.RegisterRequest;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import result.RegisterResult;
import server.ServerException;

import java.util.UUID;

public class RegisterService extends Service{

    public RegisterResult register(RegisterRequest request) throws ServerException {
        RegisterResult result=new RegisterResult();

        if (request.getUsername() == null || request.getEmail() == null || request.getPassword() == null) {
            throw new ServerException("Error: Bad Request", 400);
        }
        else if (request.getUsername().isBlank() || request.getPassword().isBlank() || request.getEmail().isBlank()) {
            throw new ServerException("Error: Bad Request", 400);
        }
        else {
            try {
                SqlUserDAO userDAO=new SqlUserDAO();
                SqlAuthDAO authDAO=new SqlAuthDAO();

                if(userDAO.getUser(request.getUsername())!=null){
                    throw new ServerException("Error: User Already Taken", 403);
                }



                userDAO.addUser(new UserData(request.getUsername(), hashPassword(request.getPassword()), request.getEmail()));
                result.setUsername(request.getUsername());

                //Creates the authToken for the User
                String tempAuthToken = UUID.randomUUID().toString();
                result.setAuthToken(tempAuthToken);
                authDAO.addAuthToken(new AuthData(tempAuthToken, request.getUsername()));
            }
            catch (DataAccessException e){
                throw new ServerException(e.getMessage(), 403);
            }

        }
        return result;
    }


}
