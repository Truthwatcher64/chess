package websocket.messages;

import chess.ChessGame;
import chess.ChessPiece;

public class LoadGame extends ServerMessage{
    private ChessGame game;

    public LoadGame(ChessGame game){
        super(ServerMessageType.LOAD_GAME);
        this.game=game;

    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}
