package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Vector;

import static ui.EscapeSequences.*;

public class ChessBoardDraw {
    public Vector<String> pieces;
    public Vector<ChessPiece> chessPieces = new Vector<>();
    private int position;
    private ChessBoard board;
    private int[] whiteNum;
    private int[] blackNum;
    private char[] whiteChars;
    private char[] blackChars;

    public ChessBoardDraw(){
        blackNum= new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        whiteNum= new int[]{8, 7, 6, 5, 4, 3, 2, 1};
        whiteChars= new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        blackChars= new char[]{'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A'};
    }

    public void printBoard(ChessGame game, String color, int rowHighlight, int columnHighlight){
        board = game.getBoard();

        //black
        if(color!=null && color.equalsIgnoreCase("black")) {
            for(int i=1; i<=8; i++){
                for(int j=8; j>=1; j--){
                    chessPieces.add(board.getPiece(new ChessPosition(i, j)));
                }
            }
            position =0;
            pieces = convertToStrings(chessPieces);
            printBoard(false, rowHighlight, columnHighlight);
            System.out.println();

        }
        //white or observer
        else{
            for(int i=8; i>=1; i--){
                for(int j=8; j>=1; j--){
                    chessPieces.add(board.getPiece(new ChessPosition(i, j)));
                }
            }
            position = 0;
            chessPieces = reversePiecesChessPieces(chessPieces);
            //System.out.println(chessPieces);
            pieces = convertToStrings(chessPieces);
            //pieces = reversePiecesStrings(pieces);

//            for(ChessPiece piece: chessPieces){
//                System.out.println(piece);
//            }
            printBoard(true, rowHighlight, columnHighlight);
            System.out.println();

        }
    }

    private Vector<String> convertToStrings(Vector<ChessPiece> chessPiecesCurrent){
        Vector<String> piecesEscapeCodes=new Vector<>();

        for(ChessPiece piece: chessPiecesCurrent){
            if(piece==null){
                piecesEscapeCodes.add(EMPTY);
            }
            else {
                switch (piece.getPieceType()) {
                    case PAWN -> piecesEscapeCodes.add(BLACK_PAWN);
                    case ROOK -> piecesEscapeCodes.add(BLACK_ROOK);
                    case KNIGHT -> piecesEscapeCodes.add(BLACK_KNIGHT);
                    case BISHOP -> piecesEscapeCodes.add(BLACK_BISHOP);
                    case KING -> piecesEscapeCodes.add(BLACK_KING);
                    case QUEEN -> piecesEscapeCodes.add(BLACK_QUEEN);
                }
            }
        }
        return piecesEscapeCodes;
    }

    private Vector<ChessPiece> reversePiecesChessPieces(Vector<ChessPiece> pieces){
        Vector<ChessPiece> temp = new Vector<>();
        for(int i =8; i > 0; i--){
            for(int j=1; j<9; j++){
                //System.out.println(board.getPiece(new ChessPosition(i, j)));
                temp.add(board.getPiece(new ChessPosition(i, j)));
            }
        }
//        for(int i = chessPieces.size()-1; i>=0; i--){
//            temp.add(pieces.elementAt(i));
//        }
        return temp;
    }

    private Vector<String> reversePiecesStrings(Vector<String> pieces){
        Vector<String> temp=new Vector<>();
        for(int i=pieces.size()-1; i>=0; i--){
            temp.add(pieces.get(i));
        }
        return temp;
    }

    private void printBoard(boolean whiteSide, int rowHighlight, int columnHighlight){
        boolean square=true;
        Vector<Integer> possMoves;
        boolean highlightCheck=false;
        if(rowHighlight>-1 && columnHighlight >-1){
            possMoves=getPossibleMoves(rowHighlight, columnHighlight);
            highlightCheck=true;
        }
        else{
            possMoves=null;
        }

        printEdge(whiteSide);

        for(int i=0; i<8; i++){
            if(i%2 == 0){
                printRow(square, i, whiteSide, highlightCheck, possMoves);
            }
            else{
                printRow(!square, i, whiteSide, highlightCheck, possMoves);
            }
        }

        printEdge(whiteSide);
    }
    private Vector<Integer> getPossibleMoves(int startingRow, int startingColumn){
        Vector<Integer> endingSpots=new Vector<>();

        ChessPiece tempPiece=(ChessPiece) board.getPiece(new ChessPosition(startingRow, startingColumn));
        if(tempPiece==null){
            return endingSpots;
        }
        Collection<ChessMove> allMoves=tempPiece.pieceMoves(board, new ChessPosition(startingRow, startingColumn));
        if(allMoves==null){
            return endingSpots;
        }

        Vector<ChessMove> allMovesReversed =new Vector<>();
        //reverse across y-axis for printing
        for(ChessMove move: allMoves){
            ChessMove moveImp=(ChessMove) move;
            switch (moveImp.getEndPosition().getRow()){
                case(1):
                    moveImp.setEndPosition(new ChessPosition(8, moveImp.getEndPosition().getColumn()));
                    break;
                case(2):
                    moveImp.setEndPosition(new ChessPosition(7, moveImp.getEndPosition().getColumn()));
                    break;
                case(3):
                    moveImp.setEndPosition(new ChessPosition(6, moveImp.getEndPosition().getColumn()));
                    break;
                case(4):
                    moveImp.setEndPosition(new ChessPosition(5, moveImp.getEndPosition().getColumn()));
                    break;
                case(5):
                    moveImp.setEndPosition(new ChessPosition(4, moveImp.getEndPosition().getColumn()));
                    break;
                case(6):
                    moveImp.setEndPosition(new ChessPosition(3, moveImp.getEndPosition().getColumn()));
                    break;
                case(7):
                    moveImp.setEndPosition(new ChessPosition(2, moveImp.getEndPosition().getColumn()));
                    break;
                case(8):
                    moveImp.setEndPosition(new ChessPosition(1, moveImp.getEndPosition().getColumn()));
                    break;
            }
            allMovesReversed.add(moveImp);

        }
        startingRow = 9- startingRow;

        endingSpots.add((startingRow-1)+ (startingColumn-1)*8);

        for(ChessMove move: allMoves){
            endingSpots.add((move.getEndPosition().getRow()-1)+(move.getEndPosition().getColumn()-1)*8);
        }
        System.out.println();
        return endingSpots;
    }

    private void printEdge(boolean whiteSide){
        char[] edgeChars;
        if(whiteSide){
            edgeChars = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        }
        else{
            edgeChars = new char[]{'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A'};
        }
        //edge
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0; i<10; i++){
            if(i>0 && i<9 && i != 5){
                System.out.print(SET_BG_COLOR_BLACK+ ui.EscapeSequences.SET_TEXT_COLOR_WHITE+edgeChars[i-1] + "   ");
            }
            else if( i == 5){
                System.out.print(SET_BG_COLOR_BLACK+ ui.EscapeSequences.SET_TEXT_COLOR_WHITE+edgeChars[i-1] + "  ");
            }

            else if(i==0){
                System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLACK + EMPTY);
            }
            else{
                System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_BLACK + EMPTY);
            }

        }
        System.out.print(RESET_BG_COLOR+ SET_TEXT_COLOR_WHITE + " \n");
    }

    private boolean printRow(boolean squareColor, int row, boolean whiteSide, boolean highlight, Vector<Integer> highlightedSquares){
        int[] columnInts;
        if(whiteSide){
            columnInts=whiteNum;
        }
        else{
            columnInts=blackNum;
        }
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //one row
        System.out.print(ui.EscapeSequences.SET_BG_COLOR_BLACK+ SET_TEXT_COLOR_WHITE + " "+columnInts[row]+" ");
        if(!highlight) {
            for (int i = 0; i < 8; i++) {
                if (squareColor) {
                    if(chessPieces.elementAt(position) == null){
                        System.out.print(SET_BG_COLOR_LIGHT_TAN + SET_TEXT_COLOR_LIGHT_TAN + getPiece(position));
                    }
                    else if(chessPieces.elementAt(position).getTeamColor() == ChessGame.TeamColor.WHITE){
                        System.out.print(SET_BG_COLOR_LIGHT_TAN + SET_TEXT_COLOR_WHITE + getPiece(position));
                    }
                    else {
                        System.out.print(SET_BG_COLOR_LIGHT_TAN + SET_TEXT_COLOR_BLACK + getPiece(position));
                    }
                    squareColor = false;
                } else {
                    if(chessPieces.elementAt(position) == null) {
                        System.out.print(SET_BG_COLOR_DARK_TAN + SET_TEXT_COLOR_DARK_TAN + getPiece(position));
                    }
                    else if(chessPieces.elementAt(position).getTeamColor() == ChessGame.TeamColor.WHITE){
                        System.out.print(SET_BG_COLOR_DARK_TAN + SET_TEXT_COLOR_WHITE + getPiece(position));
                    }
                    else {
                        System.out.print(SET_BG_COLOR_DARK_TAN + SET_TEXT_COLOR_BLACK + getPiece(position));
                    }
                    squareColor = true;
                }
            }
        }
        else {
            for(int i=0; i<8; i++){
                if(squareColor){
                    if(highlightedSquares.isEmpty()){
                        System.out.print(SET_BG_COLOR_LIGHT_TAN + getPiece(position));
                    }
                    else if(((i)*8)+row==highlightedSquares.get(0)){
                        System.out.print(SET_BG_COLOR_YELLOW+getPiece(position));
                    }
                    else if(highlightedSquares.contains(((i)*8)+row)){
                        System.out.print(SET_BG_COLOR_GREEN+getPiece(position));
                    }
                    else {
                        System.out.print(SET_BG_COLOR_LIGHT_TAN + getPiece(position));
                    }
                    squareColor=false;
                }
                else{
                    if(highlightedSquares.isEmpty()){
                        System.out.print(SET_BG_COLOR_DARK_TAN+getPiece(position));
                    }
                    else if((i*8)+row==highlightedSquares.get(0)){
                        System.out.print(SET_BG_COLOR_YELLOW+getPiece(position));
                    }
                    else if(highlightedSquares.contains((i*8)+row)){
                        System.out.print(SET_BG_COLOR_DARK_GREEN+getPiece(position));
                    }
                    else {
                        System.out.print(SET_BG_COLOR_DARK_TAN+getPiece(position));
                    }
                    squareColor=true;
                }
            }
        }
        System.out.print(SET_BG_COLOR_BLACK+SET_TEXT_COLOR_WHITE+" "+ columnInts[row]+" ");
        System.out.print(RESET_BG_COLOR+" \n");
        return squareColor;
    }

    private String getPiece(int pos){
        position++;
        return pieces.get(pos);

    }
}