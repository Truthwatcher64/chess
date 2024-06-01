package handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.Connect;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WebsocketHandler {

    class Person{
        public String authString;
        public Session session;
        public Person(String a, Session s){
            this.authString=a;
            this.session=s;
        }
    }
    Map<Integer, ArrayList<Person>> connections;

    public WebsocketHandler(){
        this.connections=new HashMap<>();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception{
        UserGameCommand command=new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()){
            case UserGameCommand.CommandType.CONNECT: {
                connect(session, message);
            }
            case UserGameCommand.CommandType.LEAVE: {
                leave(message);
            }
        }
    }

    private void connect(Session session, String msg) {
        try {
            Connect connect = new Gson().fromJson(msg, Connect.class);
            if (connections.containsKey(connect.getGameID())) {
                connections.get(connect.getGameID()).add(new Person(connect.getAuthString(), session));
            } else {
                connections.put(connect.getGameID(), new ArrayList<>());
                connections.get(connect.getGameID()).add(new Person(connect.getAuthString(), session));
            }
            SqlAuthDAO authDAO = new SqlAuthDAO();
            SqlGameDAO gameDAO = new SqlGameDAO();
            String username = authDAO.getUsername(connect.getAuthString());
            String color = null;
            if(gameDAO.getGame(connect.getGameID()).whiteUsername().equals(username)){
                color="White";
            }
            if(gameDAO.getGame(connect.getGameID()).blackUsername().equals(username)){
                color="Black";
            }
            Notification joined;
            if(color==null){
                joined = new Notification(username+" joined the game as an observer.");
            }
            else{
                joined = new Notification(username+" joined the game as "+color+".");
            }
            sendToAllOthers(new Gson().toJson(joined), connect.getGameID(), connect.getAuthString());

            LoadGame load;
            load=new LoadGame(gameDAO.getGame(connect.getGameID()).game());
            sendToMe(new Gson().toJson(load), connect.getGameID(), connect.getAuthString());

        }
        catch (Exception e){
            System.err.println("Websocket messages failed to send in the `Connect` Method.");
            e.printStackTrace();
        }
    }

    private void leave(String msg){}

    private void sendToAllOthers(String msg, int gameID, String authString) throws Exception{
        for(Person person : connections.get(gameID)){
            if(!person.authString.equals(authString)){
                person.session.getRemote().sendString(msg);
            }
        }
    }

    private void sendToAll(String msg, int gameID) throws Exception{
        for(Person person : connections.get(gameID)){
            person.session.getRemote().sendString(msg);
        }
    }

    private void sendToMe(String msg, int gameID, String authString) throws Exception{
        for(Person person : connections.get(gameID)){
            if(person.authString.equals(authString)){
                person.session.getRemote().sendString(msg);
                break;
            }
        }
    }


}
