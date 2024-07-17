package handlers;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.SqlAuthDAO;
import dataaccess.SqlGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
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
            case UserGameCommand.CommandType.MAKE_MOVE -> {
                makeMove(message, session);
            }
            case UserGameCommand.CommandType.RESIGN -> {
                resign(message);
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
                if (gameDAO.getGame(connect.getGameID()).whiteUsername() != null && gameDAO.getGame(connect.getGameID()).whiteUsername().equals(username)) {
                    color = "White";
                }
                if (gameDAO.getGame(connect.getGameID()).blackUsername() != null && gameDAO.getGame(connect.getGameID()).blackUsername().equals(username)) {
                    color = "Black";
                }

                //send to other members
                Notification joined;
                if (color == null) {
                    joined = new Notification(username + " joined the game as an observer.");
                } else {
                    joined = new Notification(username + " joined the game as " + color + ".");
                }
                sendToAllOthers(new Gson().toJson(joined), connect.getGameID(), connect.getAuthString());

                //send to me
                LoadGame load;
                load = new LoadGame(gameDAO.getGame(connect.getGameID()).game());
                sendToMe(new Gson().toJson(load), connect.getGameID(), connect.getAuthString());
            }

        }
        catch (Exception e){
            System.out.println("Dead");
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

                if (currentGame.whiteUsername() != null && currentGame.whiteUsername().equals(authDAO.getUsername(leave.getAuthString()))) {
                    gameDAO.removePlayer(authDAO.getUsername(leave.getAuthString()), ChessGame.TeamColor.WHITE, leave.getGameID());
                }
                if (currentGame.blackUsername() != null && currentGame.blackUsername().equals(authDAO.getUsername(leave.getAuthString()))) {
                    gameDAO.removePlayer(authDAO.getUsername(leave.getAuthString()), ChessGame.TeamColor.BLACK, leave.getGameID());
                }

                sendToAllOthers(new Gson().toJson(notification), leave.getGameID(), leave.getAuthString());

                Person p = null;
                for (Person person : connections.get(leave.getGameID())) {
                    if (person.authString.equals(leave.getAuthString())) {
                        p = person;
                        break;
                    }
                }
                connections.get(leave.getGameID()).remove(p);

            }
        } catch (Exception e) {
            System.out.println("Websocket messages failed to send in the 'leave' method");
            e.printStackTrace();
        }
    }

    private void makeMove(String msg, Session session){
        MakeMove makeMove = new Gson().fromJson(msg, MakeMove.class);
        try {
            SqlAuthDAO authDAO = new SqlAuthDAO();
            SqlGameDAO gameDAO = new SqlGameDAO();

            GameData currentGame = gameDAO.getGame(makeMove.getGameID());
            if(!currentGame.game().isGameState()){
                throw new Exception("Game is Over");
            }

            String name = authDAO.getUsername(makeMove.getAuthString());

            if(name == null || name.isEmpty()){
                throw new DataAccessException("Error: Unauthorized");
            }
            if(!name.equals(currentGame.whiteUsername()) && !name.equals(currentGame.blackUsername())){
                throw new Exception("Observer cannot make moves");
            }

            ChessGame.TeamColor color = currentGame.whiteUsername().equals(name) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

            if(currentGame.game().getTeamTurn() != color){
                throw new Exception("It's not your turn");
            }
            currentGame.game().makeMove(makeMove.getMove());
            gameDAO.updateGame(new Gson().toJson(currentGame.game()), makeMove.getGameID());

            LoadGame loadGame = new LoadGame(currentGame.game());

            sendToAll(new Gson().toJson(loadGame), currentGame.gameID());

            Notification notification = new Notification(name + " made the move ( " + makeMove.getMove().getStartPosition().getColumn() + ", " + makeMove.getMove().getStartPosition().getRow() + " ) " +
                    "to ( " + makeMove.getMove().getEndPosition().getColumn() + ", " + makeMove.getMove().getEndPosition().getRow() + " ) ");
            sendToAllOthers(new Gson().toJson(notification), makeMove.getGameID(), makeMove.getAuthString());

        }
        catch (DataAccessException e){
            Error error = new Error("Invalid GameID or User Unauthorized");
            try{
                session.getRemote().sendString(new Gson().toJson(error));
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        catch (InvalidMoveException e) {
            Error error = new Error("Invalid Move");
            try {
                sendToMe(new Gson().toJson(error), makeMove.getGameID(), makeMove.getAuthString());
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        catch (Exception e){
            Error error = new Error(e.getMessage());
            try {
                sendToMe(new Gson().toJson(error), makeMove.getGameID(), makeMove.getAuthString());
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }


    }

    private void resign(String msg){
        Resign resign = new Gson().fromJson(msg, Resign.class);
        try{
            SqlAuthDAO authDAO = new SqlAuthDAO();
            SqlGameDAO gameDAO = new SqlGameDAO();
            GameData gamedata = gameDAO.getGame(resign.getGameID());

            if(!gamedata.game().isGameState()){
                throw new Exception("Game is Over");
            }

            String name = authDAO.getUsername(resign.getAuthString());
            if(name == null || name.isBlank()){
                throw new Exception("Error: Unauthorized");
            }

            if(!gamedata.whiteUsername().equals(name) && !gamedata.blackUsername().equals(name)){
                throw new Exception("Observer cannot resign");
            }

            Notification notification = new Notification(name+" has resigned");
            sendToAll(new Gson().toJson(notification), resign.getGameID());

            //Change game state
            gamedata.game().setGameState(false);
            String gameJSON = new Gson().toJson(gamedata.game());
            gameDAO.updateGame(gameJSON, resign.getGameID());

        }
        catch (Exception e){
            Error error = new Error(e.getMessage());
            try {
                sendToMe(new Gson().toJson(error), resign.getGameID(), resign.getAuthString());
            }
            catch (Exception ex){
                System.out.println(ex.getMessage());
            }
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
