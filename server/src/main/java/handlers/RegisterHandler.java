package handlers;

import com.google.gson.Gson;
import result.*;
import request.*;
import server.ServerException;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends Handler{

//    public Object handleRequest(Request req, Response res, DatabaseManager data){
//        RegisterRequest request= new Gson().fromJson(req.body(), RegisterRequest.class);
//        String body=null;
//        try {
//
//            //make service
//            RegisterService service = new RegisterService();
//            RegisterResult result = service.register(request, data);
//            //send json
//            body = new Gson().toJson(result);
//            setFinalError(result.getMessage(), res);
//            res.body(body);
//        }
//        catch (ServerException e){
//            body=e.getMessage();
//            res.body(e.getMessage());
//            res.status(e.getErrorCode());
//        }
//
//        return body;
//    }

    @Override
    public Object handleRequest(Request req, Response res) {
        RegisterRequest request= new Gson().fromJson(req.body(), RegisterRequest.class);
        String body=null;
        RegisterResult result=new RegisterResult();
        try {

            //make service
            RegisterService service = new RegisterService();
            result = service.register(request);
            //send json
            body = new Gson().toJson(result);
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
