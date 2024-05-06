package service;

import dataaccess.DataAccessException;
import dataaccess.SqlAuthDAO;
import org.mindrot.jbcrypt.BCrypt;
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

    protected String hashPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        return hashedPassword;
    }

    protected boolean verifyUser(String hashedPassword, String clearPassword) {
        return BCrypt.checkpw(clearPassword, hashedPassword);
    }
}
