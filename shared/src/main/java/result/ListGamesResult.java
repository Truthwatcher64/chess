package result;


import model.GameData;

import java.util.List;


/**
 * Represents the Result that comes from the List Game Command
 */
public class ListGamesResult extends Result{
    /**
     * List of instances of Games in the database
     */
    private List<GameData> games;

    /**
     * Creates the result, no parameter
     */
    public ListGamesResult(){

    }

    public List<GameData> getGame() {
        return games;
    }

    public void setGame(List<GameData> games) {
        this.games = games;
    }
}
