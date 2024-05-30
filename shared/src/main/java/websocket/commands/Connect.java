package websocket.commands;

public class Connect extends UserGameCommand{
    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    private int gameID;
    public Connect(String authToken, int gameID){
        super(authToken);
        this.commandType=CommandType.CONNECT;
        this.gameID=gameID;

    }

}
