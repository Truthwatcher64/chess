package handlers;

import result.ListGamesResult;
import com.google.gson.Gson;
import model.GameData;
import server.ServerException;
import service.ListService;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListGameHandler extends Handler{
    @Override
    public Object handleRequest(Request req, Response res) {
        ListGamesResult result=new ListGamesResult();
        String body=null;
        ListService service=new ListService();
        Collection<GameData> temp;
        List<GameData> list=new ArrayList<>();
        try{
            temp = service.listGames(req.headers("authorization"));
            list.addAll(temp);
            result.setGame(list);
            body=new Gson().toJson(result);
            res.body(body);
        }
        catch (ServerException e){
            result.setMessage(e.getMessage());
            body=new Gson().toJson(result);
            res.body(body);
            res.status(e.getErrorCode());
        }
        return body;
    }
}
