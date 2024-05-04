package service;


import Result.Result;
import dataAccess.AuthDAO;
import dataAccess.*;
import server.ServerException;

/**
 * Class for controlling the clear operation for the database
 */
public class ClearService {

    /**
     * Attempts to clear the database of all values
     * @return result of an error or nothing if it is a success
     */
//    public Result clear(DatabaseManager data) throws ServerException {
//        Result result=new Result();
//        try {
//            new SqlUserDAO().clear();
//            new SqlAuthDAO().clear();
//            new SqlGameDAO().clear();
//        }
//        catch(DataAccessException e){
//            throw new ServerException("Error: Failure to clear", 500);
//        }
//        result.setMessage("Database Cleared");
//        return new Result();
//    }

    public Result clear() throws ServerException {
        Result result=new Result();
        try {
            new SqlUserDAO().clear();
            new SqlAuthDAO().clear();
            new SqlGameDAO().clear();
        }
        catch(DataAccessException e){
            throw new ServerException("Error: Failure to clear", 500);
        }
        result.setMessage("Database Cleared");
        return new Result();
    }
}
