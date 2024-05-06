package handlers;

import result.Result;
import request.JoinRequest;
import com.google.gson.Gson;
import server.ServerException;
import service.JoinService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler{
    @Override
    public Object handleRequest(Request req, Response res) {
        Result result=new Result();
        String body=null;
        JoinService service=new JoinService();
        JoinRequest request=new Gson().fromJson(req.body(), JoinRequest.class);
        String authToken=req.headers("authorization");
        try{
            service.join(request, authToken);
            body="{}";
        }
        catch (ServerException e){
            result.setMessage(e.getMessage());
            body=new Gson().toJson(result);
            res.status(e.getErrorCode());
            res.body(body);
        }
        return body;
    }
}
