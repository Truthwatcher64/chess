package service;
import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import server.ServerException;

public class LogoutService {
    public void logout(String authToken) throws ServerException{
        SqlAuthDAO authDAO=new SqlAuthDAO();

        try{
            //invalid check
            if(authToken==null || authDAO.getAuthToken(authToken)==null) {
                throw new ServerException("Error: Unauthorized", 401);
            }
            authDAO.deleteAuthToken(authToken);
        }
        catch (DataAccessException e){
            throw new ServerException(e.getMessage(), 500);
        }
    }
}
