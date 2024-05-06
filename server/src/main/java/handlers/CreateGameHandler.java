package handlers;

import Result.CreateResult;
import Result.Result;
import com.google.gson.Gson;
import server.ServerException;
import service.CreateService;
import spark.Request;
import spark.Response;
import request.CreateRequest;

public class CreateGameHandler extends Handler{

    @Override
    public Object handleRequest(Request req, Response res) {
        CreateRequest request=new Gson().fromJson(req.body(), CreateRequest.class);
        request.setAuthToken(req.headers("authorization"));
        CreateService service=new CreateService();
        CreateResult result = new CreateResult();
        String body=null;
        try{
            result=service.create(request);
            body=new Gson().toJson(result);
            res.body(body);
        }
        catch (ServerException e){
            Result result2=new Result();
            result2.setMessage(e.getMessage());
            body=new Gson().toJson(result2);
            res.body(body);
            res.status(e.getErrorCode());
        }
        return body;
    }
}
