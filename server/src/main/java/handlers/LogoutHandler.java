package handlers;

import Result.Result;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SqlAuthDAO;
import server.ServerException;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler extends Handler{
    @Override
    public Object handleRequest(Request req, Response res) {
        Result result=new Result();
        try{
            LogoutService service = new LogoutService();
            service.logout(req.headers("authorization"));
            res.status(200);
            res.body("{}");
        }
        catch (ServerException e){
            result.setMessage(e.getMessage());
            res.body(new Gson().toJson(result));
            res.status(e.getErrorCode());
        }
        return res.body();
    }
}
