package request;
/**
 * Request sent to the database to create a game
 */
public class CreateRequest {
    /**
     * Name for the new game
     */
    private String gameName;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private String authToken;

    /**
     * Attempts to create a new game with the gameName
     * //@param gameName new name for the game
     */
    public CreateRequest(){

    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
