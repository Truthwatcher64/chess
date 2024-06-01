package server;

import dataaccess.DatabaseManager;
import handlers.*;
import spark.*;

public class Server {
    private DatabaseManager database;

    public int run(int desiredPort) {
        try{
            this.database=new DatabaseManager();
        }catch (Exception e){
            System.err.println("Error: The database failed to setup and the server can't run properly");
            System.err.println(e.getMessage());
        }


        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.


//        // Register handlers for each endpoint using the method reference syntax
        Spark.webSocket("/ws", new WebsocketHandler());
        Spark.delete("/db", ((request, response) -> new ClearHandler().handleRequest(request, response)));
        Spark.post("/user", ((request, response) -> new RegisterHandler().handleRequest(request, response)));
        Spark.post("/session", ((request, response) -> new LoginHandler().handleRequest(request, response)));
        Spark.delete("/session", ((request, response) -> new LogoutHandler().handleRequest(request, response)));
        Spark.get("/game", ((request, response) -> new ListGameHandler().handleRequest(request, response)));
        Spark.post("/game", (request, response) -> new CreateGameHandler().handleRequest(request, response));
        Spark.put("/game", (request, response) -> new JoinGameHandler().handleRequest(request, response));
        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
