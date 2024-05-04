package Request;

/**
 * Request sent to join a game
 */
public class JoinRequest {
    /**
     * GameID of the game to join
     */
    private int gameID;
    /**
     * Color that the user will join with
     */
    private String playerColor;

    private String userName;

    /**
     * Request that is to be sent
     */
    public JoinRequest(){

    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }
}
