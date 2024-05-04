package service;

import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import server.ServerException;

public class Service {
    protected void checkAuthorization(String authToken) throws ServerException {
        if (authToken == null ) {
            throw new ServerException("Error: Unauthorized", 401);
        }
        else{
            try {
                SqlAuthDAO authDAO = new SqlAuthDAO();
                if (authDAO.getUsername(authToken) == null) {
                    throw new ServerException("Error: Unauthorized", 401);
                }
            }
            catch(DataAccessException e){
                throw new ServerException(e.getMessage(), 500);
            }

        }
    }
}
