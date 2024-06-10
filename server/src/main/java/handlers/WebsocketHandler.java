package handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.Connect;
import websocket.commands.Leave;
import websocket.commands.UserGameCommand;
import websocket.messages.Error;
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
    static Map<Integer, ArrayList<Person>> connections;

    public WebsocketHandler(){
        this.connections=new HashMap<>();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception{
        UserGameCommand command=new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()){
            case UserGameCommand.CommandType.CONNECT -> {
                connect(session, message);
            }
            case UserGameCommand.CommandType.LEAVE -> {
                leave(message);
            }
        }
    }

    private void connect(Session session, String msg) {
        try {
            SqlAuthDAO authDAO = new SqlAuthDAO();
            SqlGameDAO gameDAO = new SqlGameDAO();
            Connect connect = new Gson().fromJson(msg, Connect.class);

            if(gameDAO.getGame(connect.getGameID()) == null){
                Error error = new Error("Error: incorrect GameID");
                String json = new Gson().toJson(error);
                session.getRemote().sendString(json);
                //sendToMe(json, connect.getGameID(), connect.getAuthString());
            }
            else if(authDAO.getUsername(connect.getAuthString()) ==  null){
                Error error = new Error("Error: Unauthorized");
                String json = new Gson().toJson(error);
                session.getRemote().sendString(json);
            }
            else {

                if (connections.containsKey(connect.getGameID())) {
                    connections.get(connect.getGameID()).add(new Person(connect.getAuthString(), session));
                } else {
                    connections.put(connect.getGameID(), new ArrayList<>());
                    connections.get(connect.getGameID()).add(new Person(connect.getAuthString(), session));
                }

                String username = authDAO.getUsername(connect.getAuthString());
                String color = null;
                if (gameDAO.getGame(connect.getGameID()).whiteUsername().equals(username)) {
                    color = "White";
                }
                if (gameDAO.getGame(connect.getGameID()).blackUsername().equals(username)) {
                    color = "Black";
                }
                Notification joined;
                if (color == null) {
                    joined = new Notification(username + " joined the game as an observer.");
                } else {
                    joined = new Notification(username + " joined the game as " + color + ".");
                }
                sendToAllOthers(new Gson().toJson(joined), connect.getGameID(), connect.getAuthString());

                LoadGame load;
                load = new LoadGame(gameDAO.getGame(connect.getGameID()).game());
                sendToMe(new Gson().toJson(load), connect.getGameID(), connect.getAuthString());
            }

        }
        catch (Exception e){
            System.err.println("Websocket messages failed to send in the `Connect` Method.");
            e.printStackTrace();
        }
    }

    private void leave(String msg){
        try {
            SqlAuthDAO authDAO = new SqlAuthDAO();
            SqlGameDAO gameDAO = new SqlGameDAO();
            Leave leave = new Gson().fromJson(msg, Leave.class);
            GameData currentGame = gameDAO.getGame(leave.getGameID());

            if (gameDAO.getGame(leave.getGameID()) != null) {
                System.out.println("Good");
                String note = authDAO.getUsername(leave.getAuthString()) + " left the game";
                Notification notification = new Notification(note);

                if (currentGame.whiteUsername().equals(authDAO.getUsername(leave.getAuthString()))) {
                    gameDAO.removePlayer(authDAO.getUsername(leave.getAuthString()), ChessGame.TeamColor.WHITE, leave.getGameID());
                }
                if (currentGame.blackUsername().equals(authDAO.getUsername(leave.getAuthString()))) {
                    gameDAO.removePlayer(authDAO.getUsername(leave.getAuthString()), ChessGame.TeamColor.BLACK, leave.getGameID());
                }

                sendToAllOthers(new Gson().toJson(notification), leave.getGameID(), leave.getAuthString());

                Person p = null;
                for (Person person : connections.get(leave.getGameID())) {
                    if (person.authString.equals(leave.getAuthString())) {
                        p = person;
                    }
                }
                connections.get(leave.getGameID()).remove(p);

            }
        } catch (Exception e) {
            System.out.println("Websocket messages failed to send in the 'lenve' method");
            e.printStackTrace();
        }
    }


    private void sendToAllOthers(String msg, int gameID, String authString) throws Exception{
        ArrayList<Person> toRemove=new ArrayList<>();
        for(Person person : connections.get(gameID)){
            if(!person.authString.equals(authString)){
                if(person.session.isOpen()) {
                    person.session.getRemote().sendString(msg);
                }
                else{
                    toRemove.add(person);
                }
            }
        }
        for(Person person : toRemove){
            connections.get(gameID).remove(person);
        }
    }

    private void sendToAll(String msg, int gameID) throws Exception{
        ArrayList<Person> toRemove=new ArrayList<>();
        for(Person person : connections.get(gameID)){
            if(person.session.isOpen()) {
                person.session.getRemote().sendString(msg);
            }
            else{
                toRemove.add(person);
            }
        }
        for(Person person : toRemove){
            connections.get(gameID).remove(person);
        }
    }

    private void sendToMe(String msg, int gameID, String authString) throws Exception{
        for(Person person : connections.get(gameID)){
            if(person.authString.equals(authString)){
                if(person.session.isOpen()) {
                    person.session.getRemote().sendString(msg);
                    break;
                }
            }
        }
    }


}
