package handlers;

import result.Result;
import com.google.gson.Gson;
import server.ServerException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler extends Handler{
    public ClearHandler(){

    }
//    @Override
//    public Object handleRequest(Request req, Response res, DatabaseManager data){
//        String body;
//        Result result;
//        try {
//            ClearService service = new ClearService();
//            result = service.clear(data);
//            body = new Gson().toJson(result);
//            res.status(200);
//        }
//        catch (ServerException e){
//            body=e.getMessage();
//            res.status(e.getErrorCode());
//
//        }
//
//        //setFinalError(result.getMessage(), res);
//
//        res.body(body);
//        return body;
//    }

    @Override
    public Object handleRequest(Request req, Response res) {
        String body;
        Result result;
        try {
            ClearService service = new ClearService();
            result = service.clear();
            body = new Gson().toJson(result);
            res.status(200);
        }
        catch (ServerException e){
            body=e.getMessage();
            res.status(e.getErrorCode());

        }

        //setFinalError(result.getMessage(), res);

        res.body(body);
        return body;
    }
}
