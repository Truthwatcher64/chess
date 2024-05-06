package result;
/**
 * Result from a create request for a game.
 */
public class CreateResult extends Result{
    /**
     * New gameID for the newly create game
     */
    private int gameID;

    /**
     * Generates the result, no parameters
     */
    public CreateResult(){

    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
