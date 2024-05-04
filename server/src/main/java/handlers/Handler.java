package handlers;

import dataAccess.DatabaseManager;
import dataAccess.FakeData;
import dataAccess.SqlAuthDAO;
import model.AuthData;
import server.ServerException;
import spark.Request;
import spark.Response;

public abstract class Handler {
    public Handler(){

    }

    protected void setFinalError(String message, Response res){
        if(message!=null && message.contains("Error") && res.status()<=200){
            System.out.println(message);
            res.status(500);
        }
    }

//    protected abstract Object handleRequest(Request req, Response res, DatabaseManager data);

    protected abstract Object handleRequest(Request req, Response res);
}
