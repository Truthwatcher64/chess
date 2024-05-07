package handlers;

import spark.Request;
import spark.Response;

public abstract class Handler {
    public Handler(){

    }

    protected void setFinalError(String message, Response res){
        if(message!=null && message.contains("Error") && res.status()<=200){
            System.err.println(message);
            res.status(500);
        }
    }

//    protected abstract Object handleRequest(Request req, Response res, DatabaseManager data);

    protected abstract Object handleRequest(Request req, Response res);
}
