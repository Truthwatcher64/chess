package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GameRules {
    private ChessPiece piece;
    private ChessPosition start;
    private ChessBoard board;
    public GameRules(){

    }
    public Collection<ChessMove> enterPiece(ChessPiece p, ChessPosition s, ChessBoard b) {
        this.piece=p;
        this.start=s;
        this.board=b;
        Set<ChessMove> possMoves=new HashSet<>();
        possMoves=findMoves(possMoves);

        return possMoves;
    }
    public Set<ChessMove> findMoves(Collection<ChessMove> possibleMoves){
        Set<ChessMove> realMoves=new HashSet<>();
        ChessPiece.PieceType type=piece.getPieceType();
        if(type!=null) {
            switch (type) {
                case PAWN:
                    pawnMoves(piece, possibleMoves);
                    break;

                case ROOK:
                    rookMoves(possibleMoves);
                    break;

                case KNIGHT:
                    knightMoves(possibleMoves);
                    break;

                case BISHOP:
                    bishopMoves(possibleMoves);
                    break;

                case KING:
                    kingMoves(possibleMoves);
                    break;

                case QUEEN:
                    queenMoves(possibleMoves);
                    break;
            }

            dontMoveOverSameColor(piece, possibleMoves, realMoves);


        }
        return realMoves;
    }

    private void dontMoveOverSameColor(ChessPiece piece, Collection<ChessMove> possibleMoves, Set<ChessMove> realMoves) {
        Iterator<ChessMove> itr= possibleMoves.iterator();
        ChessMove tempMove;

        while(itr.hasNext()){
           tempMove=itr.next();

            if((tempMove)!=null){

                //empty spot on board
                if (board.getPiece((tempMove.getEndPosition())) == null)
                {
                    realMoves.add(tempMove);
                }
                else {
                    //another check for empty spot
                    if (board.getPiece(tempMove.getEndPosition()).getTeamColor() == null) {
                        realMoves.add(tempMove);
                    }
                    else {
                        //potential capture, if piece is opponents piece
                        if (board.getPiece((tempMove.getEndPosition())).getTeamColor() != piece.getTeamColor()) {
                            realMoves.add(tempMove);
                        }
                    }
                }
            }
        }
    }

    private void queenMoves(Collection<ChessMove> possibleMoves) {

        rookMoves(possibleMoves);

        bishopMoves(possibleMoves);

    }

    private void kingMoves(Collection<ChessMove> possibleMoves) {
        //We actually can use the same method as the knight just with different numbers
        //one move straight
        possibleMoves.add(knightMoveHelper(-1, 0));
        possibleMoves.add(knightMoveHelper(1, 0));
        possibleMoves.add(knightMoveHelper(0, -1));
        possibleMoves.add(knightMoveHelper(0, 1));

        //one move diagonally
        possibleMoves.add(knightMoveHelper(-1, -1));
        possibleMoves.add(knightMoveHelper(-1, 1));
        possibleMoves.add(knightMoveHelper(1, -1));
        possibleMoves.add(knightMoveHelper(1, 1));
    }


    private void bishopMoves(Collection<ChessMove> possibleMoves) {

        bishopMoveHelper(possibleMoves, 1, 1);
        bishopMoveHelper(possibleMoves, 1, -1);
        bishopMoveHelper(possibleMoves, -1, 1);
        bishopMoveHelper(possibleMoves, -1, -1);
    }
    private void bishopMoveHelper(Collection<ChessMove> possibleMoves, int rowDirection, int columnDirection){
        ChessPosition tempPoss;
        int rowCount= start.getRow()+rowDirection;
        int columnCount= start.getColumn()+columnDirection;
        while(rowCount<9 && columnCount<9 && rowCount>0 && columnCount>0){
            tempPoss=new ChessPosition(rowCount, columnCount);
            possibleMoves.add(new ChessMove(start, tempPoss, null));
            rowCount=rowCount+rowDirection;
            columnCount=columnCount+columnDirection;
            if(board.getPiece(tempPoss) != null) {
                if (board.getPiece(tempPoss).getTeamColor() != null) {
                    break;
                }
            }
        }
    }

    private void knightMoves(Collection<ChessMove> possibleMoves) {

        //eight moves, each a combination of a 1 and 2 distance move
        possibleMoves.add(knightMoveHelper(2, 1));
        possibleMoves.add(knightMoveHelper(2, -1));
        possibleMoves.add(knightMoveHelper(1, 2));
        possibleMoves.add(knightMoveHelper(1, -2));
        possibleMoves.add(knightMoveHelper(-2, 1));
        possibleMoves.add(knightMoveHelper(-2, -1));
        possibleMoves.add(knightMoveHelper(-1, 2));
        possibleMoves.add(knightMoveHelper(-1, -2));

    }
    private ChessMove knightMoveHelper(int row, int column){
        ChessPosition tempPoss;
        int rowCount= start.getRow()+row;
        int columnCount= start.getColumn()+column;
        if(rowCount<9 && rowCount>0 && columnCount<9 && columnCount>0){
            tempPoss=new ChessPosition(rowCount, columnCount);
            return new ChessMove(start, tempPoss, null);
        }
        return null;
    }

    private void rookMoves(Collection<ChessMove> possibleMoves) {

        rookHelper(possibleMoves,1, 0);
        rookHelper(possibleMoves,-1, 0);
        rookHelper(possibleMoves,0, 1);
        rookHelper(possibleMoves,0, -1);

    }
    private void rookHelper(Collection<ChessMove> possibleMoves, int rowDirection, int columnDirection){
        ChessPosition tempPoss;
        int rowCount=start.getRow()+rowDirection;
        int columnCount=start.getColumn()+columnDirection;
        while(rowCount>0 && rowCount<9 && columnCount>0 && columnCount<9){
            tempPoss=new ChessPosition(rowCount, columnCount);
            possibleMoves.add(new ChessMove(start, tempPoss, null));
            if(board.getPiece(tempPoss) != null) {
                if (board.getPiece(tempPoss).getTeamColor() != null) {
                    break;
                }
            }
            rowCount=rowCount+rowDirection;
            columnCount=columnCount+columnDirection;
        }
    }

    private void pawnMoves(ChessPiece piece, Collection<ChessMove> possibleMoves) {
        //white pawn moves
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            whitePawnMoves(piece, possibleMoves);
        }
        //black pawn moves
        else {
            blackPawnMoves(piece, possibleMoves);
        }
    }

    private void blackPawnMoves(ChessPiece piece, Collection<ChessMove> possibleMoves) {
        if (start.getRow() == 7) {
            pawnCapture(piece, possibleMoves, board, -1, false);
            pawnStraightMove(possibleMoves, board, false, -2);
        }
        else if (start.getRow() == 2) {
            promotion(piece, possibleMoves, board, -1);
        }
        else {
            pawnCapture(piece, possibleMoves, board, -1, false);
            pawnStraightMove(possibleMoves, board, false, -1);
        }
    }

    private void whitePawnMoves(ChessPiece piece, Collection<ChessMove> possibleMoves) {
        if (start.getRow() == 2) {
            pawnCapture(piece, possibleMoves, board, 1, false);
            pawnStraightMove(possibleMoves, board, false, 2);
        }
        else if (start.getRow() == 7) {
            promotion(piece, possibleMoves, board, 1);

        }
        else {
            pawnCapture(piece, possibleMoves, board, 1, false);
            pawnStraightMove(possibleMoves, board, false, 1);
        }
    }

    private void pawnStraightMove(Collection<ChessMove> possibleMoves, ChessBoard board, boolean promotion, int distance) {
        ChessPosition tempPoss;
        int tempDistance;
        if(distance== 2 || distance==-2){
            tempDistance=distance/2;
        }
        else{
            tempDistance=distance;
        }
        tempPoss = new ChessPosition(start.getRow() + (tempDistance), start.getColumn());
        if (board.getPiece(tempPoss) == null) {
            promoteDirectly(tempPoss, possibleMoves, promotion);

        }
        if((distance==2 || distance==-2) && board.getPiece(tempPoss)==null){
            tempPoss = new ChessPosition(start.getRow() + distance, start.getColumn());
            if (board.getPiece(tempPoss) == null) {
                possibleMoves.add(new ChessMove(start, tempPoss, null));
            }
        }
    }

    private void pawnCapture(ChessPiece piece, Collection<ChessMove> possibleMoves, ChessBoard board, int direction, boolean promotion) {
        ChessPosition tempPoss;
        //capture to right
        if(start.getColumn()+1 < 8) {
            if (board.getPiece(new ChessPosition(start.getRow() + direction, start.getColumn() + 1)) != null &&
                    board.getPiece(new ChessPosition(start.getRow() + direction, start.getColumn() + 1)).getPieceType() != null &&
                    board.getPiece(new ChessPosition(start.getRow() + direction, start.getColumn() + 1)).getTeamColor() != piece.getTeamColor()) {

                tempPoss = new ChessPosition(start.getRow() + direction, start.getColumn() + 1);
                promoteDirectly(tempPoss, possibleMoves, promotion);
            }
        }
        //capture to left
        if(start.getColumn()-1 > 0) {
            if (board.getPiece(new ChessPosition(start.getRow() + direction, start.getColumn() - 1)) != null &&
                    board.getPiece(new ChessPosition(start.getRow() + direction, start.getColumn() - 1)).getPieceType() != null &&
                    board.getPiece(new ChessPosition(start.getRow() + direction, start.getColumn() - 1)).getTeamColor() != piece.getTeamColor()) {

                tempPoss = new ChessPosition(start.getRow() + direction, start.getColumn() - 1);
                promoteDirectly(tempPoss, possibleMoves, promotion);
            }
        }
    }

    /*
    Actually preforms the pawn move. Determines if it is a promotion move or not
     */
    private void promoteDirectly(ChessPosition tempPoss, Collection<ChessMove> possibleMoves, boolean promotion){
        if(!promotion) {
            possibleMoves.add(new ChessMove(start, tempPoss, null));
        }
        else{
            possibleMoves.add(new ChessMove(start, tempPoss, ChessPiece.PieceType.QUEEN));
            possibleMoves.add(new ChessMove(start, tempPoss, ChessPiece.PieceType.KNIGHT));
            possibleMoves.add(new ChessMove(start, tempPoss, ChessPiece.PieceType.BISHOP));
            possibleMoves.add(new ChessMove(start, tempPoss, ChessPiece.PieceType.ROOK));
        }
    }

    private void promotion(ChessPiece piece, Collection<ChessMove> possibleMoves, ChessBoard board, int direction) {
        pawnCapture(piece, possibleMoves, board, direction, true);

        pawnStraightMove(possibleMoves, board, true, direction);

    }
}
