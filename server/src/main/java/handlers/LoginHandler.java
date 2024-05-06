package handlers;

import service.LoginService;
import result.LoginResult;
import com.google.gson.Gson;
import server.ServerException;
import spark.Request;
import spark.Response;

import request.*;

public class LoginHandler extends Handler{
    @Override
    public Object handleRequest(Request req, Response res) {
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        String body=null;
        LoginResult result=new LoginResult();
        try{
            LoginService service=new LoginService();
            result=service.login(request);

            body=new Gson().toJson(result);
            setFinalError(result.getMessage(), res);
            res.body(body);

        }
        catch (ServerException e){
            result.setMessage(e.getMessage());
            body=new Gson().toJson(result);
            res.body(e.getMessage());
            res.status(e.getErrorCode());
            System.out.println(e.getMessage());
        }
        return body;
    }

}
