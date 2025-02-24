package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
    private int gameID;
    private ChessMove move;
    public MakeMove(String authToken, int gameID, ChessMove move){
        super(authToken);
        this.gameID=gameID;
        this.move=move;
        this.commandType=CommandType.MAKE_MOVE;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    public void setMove(ChessMove move) {
        this.move = move;
    }
}
