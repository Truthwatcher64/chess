package client;

import chess.ChessGame;
import ui.ChessBoardDraw;
import ui.EscapeSequences;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

import static ui.EscapeSequences.*;


/*
Test printout of the board
 */
public class ChessBoardConsole {

    public static void main(String[] args) {
        ChessBoardDraw draw = new ChessBoardDraw();
        ChessGame game = new ChessGame();

        draw.printBoard(game, "black", -1, -1);
        draw.printBoard(game, "white", -1, -1);
    }

}