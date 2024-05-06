package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard fullBoard;
    TeamColor activecColor;
    Set<ChessPiece> whitePieces;
    Set<ChessPiece> blackPieces;
    private ChessPiece lastPiece;
    private ChessMove lastMove;
    private ChessMove enPassMove;
    private ArrayList<ChessMove> allGameMoves;

    public ChessGame() {
        fullBoard=new ChessBoard();
        fullBoard.resetBoard();
        setTeamTurn(TeamColor.WHITE);
        lastMove=new ChessMove(new ChessPosition(-1, -1), new ChessPosition(-1, -1), null);
        lastPiece=new ChessPiece(null, ChessPiece.PieceType.PAWN);
        allGameMoves=new ArrayList<ChessMove>();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return activecColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.activecColor=team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece tempPiece;
        tempPiece= fullBoard.getPiece(startPosition);
        Collection<ChessMove> possibleMoves=new HashSet<>();
        if(tempPiece==null){
            return possibleMoves;
        }

        //generate possible moves
        possibleMoves=tempPiece.pieceMoves(fullBoard, startPosition);

        //add enpassant
        possibleMoves=enpassantMoves(possibleMoves, tempPiece, startPosition);

        //moves off of board or into check
        possibleMoves=removeInvalidMoves(possibleMoves, tempPiece.getTeamColor());


        return possibleMoves;
    }

    private Collection<ChessMove> removeInvalidMoves(Collection<ChessMove> oldPossibleMoves, TeamColor teamColor){
        //is a move off the board or in check
        //creates a second list of all these invalid moves
        //removes all in the second list from the first
        ChessPosition start;
        ChessPosition end;
        ChessPiece piece;
        ChessPiece endPiece;
        Collection<ChessMove> tempMoves=new HashSet<>();
        for(ChessMove move: oldPossibleMoves) {
            start = (ChessPosition) move.getStartPosition();
            end = (ChessPosition) move.getEndPosition();
            piece = (ChessPiece) fullBoard.getPiece(start);
            endPiece = (ChessPiece) fullBoard.getPiece(end);

            //make the temporary move
            fullBoard.addPiece(end, piece);
            fullBoard.addPiece(start, new ChessPiece(null, null));

            if (isInCheck(teamColor)) {
                tempMoves.add(move);
            }

            //move back
            fullBoard.addPiece(start, piece);
            fullBoard.addPiece(end, endPiece);
        }

        for(ChessMove move: tempMoves){
            oldPossibleMoves.remove(move);
        }
        return oldPossibleMoves;
    }

    private Collection<ChessMove> enpassantMoves(Collection<ChessMove> oldPossibleMoves, ChessPiece tempPiece, ChessPosition startPosition){

        if(tempPiece.getPieceType()== ChessPiece.PieceType.PAWN && startPosition.getRow()==5 && tempPiece.getTeamColor()==TeamColor.WHITE){

            if(lastPiece!=null) {
                if (lastPiece.getPieceType() == ChessPiece.PieceType.PAWN && lastMove.getEndPosition().getRow() == startPosition.getRow()  && lastMove.getEndPosition().getColumn() == startPosition.getColumn() + 1) {
                    enPassMove=new ChessMove(new ChessPosition(startPosition.getRow(), startPosition.getColumn()), new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1), null);
                    oldPossibleMoves.add(new ChessMove(new ChessPosition(startPosition.getRow(), startPosition.getColumn()), new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()+1), null));
                }
                else if (lastPiece.getPieceType() == ChessPiece.PieceType.PAWN && lastMove.getEndPosition().getRow() == startPosition.getRow()  && lastMove.getEndPosition().getColumn() == startPosition.getColumn() - 1) {
                    enPassMove=new ChessMove(new ChessPosition(startPosition.getRow(), startPosition.getColumn()), new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1), null);
                    oldPossibleMoves.add(new ChessMove(new ChessPosition(startPosition.getRow(), startPosition.getColumn()), new ChessPosition(startPosition.getRow()+1, startPosition.getColumn()-1), null));
                }
            }
        }
        if(tempPiece.getPieceType()== ChessPiece.PieceType.PAWN&&startPosition.getRow()==4 && tempPiece.getTeamColor()==TeamColor.BLACK){
            if(lastPiece!=null) {
                if (lastPiece.getPieceType() == ChessPiece.PieceType.PAWN && lastMove.getEndPosition().getRow() == startPosition.getRow() && lastMove.getEndPosition().getColumn() == startPosition.getColumn() + 1) {
                    enPassMove=(new ChessMove(new ChessPosition(startPosition.getRow(), startPosition.getColumn()), new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1), null));
                    oldPossibleMoves.add(new ChessMove(new ChessPosition(startPosition.getRow(), startPosition.getColumn()), new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()+1), null));
                }
                else if (lastPiece.getPieceType() == ChessPiece.PieceType.PAWN && lastMove.getEndPosition().getRow() == startPosition.getRow() && lastMove.getEndPosition().getColumn() == startPosition.getColumn() - 1) {
                    enPassMove=(new ChessMove(new ChessPosition(startPosition.getRow(), startPosition.getColumn()), new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1), null));
                    oldPossibleMoves.add(new ChessMove(new ChessPosition(startPosition.getRow(), startPosition.getColumn()), new ChessPosition(startPosition.getRow()-1, startPosition.getColumn()-1), null));

                }
            }
        }
        return oldPossibleMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece piece=fullBoard.getPiece(move.getStartPosition());
        ChessPosition start = (ChessPosition) move.getStartPosition();
        ChessPosition end = (ChessPosition) move.getEndPosition();
        ChessPiece endPiece= new ChessPiece(null, null);

        checkForErrors(move, start, end, endPiece);

        //check what is at the end spot
        fullBoard.addPiece(end, piece);
        fullBoard.addPiece(start, null);

        doPawnStuff(end, move);

        if (isInCheck(activecColor)) {
            fullBoard.addPiece(end, endPiece);
            fullBoard.addPiece(start, piece);
            throw new InvalidMoveException("Moved into check, or didn't move out of check");
        }

        endOfTurnStuff(move);
    }

    private void endOfTurnStuff(ChessMove move){
        if (activecColor == TeamColor.WHITE)
            setTeamTurn(TeamColor.BLACK);
        else{
            setTeamTurn(TeamColor.WHITE);
        }

        //update the game record
        lastMove=(ChessMove) move;
        lastPiece=(ChessPiece) fullBoard.getPiece(move.getEndPosition());
        allGameMoves.add(move);
    }

    private void checkForErrors(ChessMove move, ChessPosition start, ChessPosition end, ChessPiece endPiece) throws InvalidMoveException{
        if (move.getStartPosition().getColumn() < 0 || move.getStartPosition().getColumn() > 7) {
            if (move.getStartPosition().getRow() < 0 || move.getStartPosition().getRow() > 7) {
                throw new InvalidMoveException("Start Location is off the board");
            }
        }
        if (move.getEndPosition().getColumn() < 0 || move.getEndPosition().getColumn() > 7) {
            if (move.getEndPosition().getRow() < 0 || move.getEndPosition().getRow() > 7) {
                throw new InvalidMoveException("End Location is off the board");
            }
        }

        if (fullBoard.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No Piece on Start Location");
        }

        if (fullBoard.getPiece(move.getStartPosition()).getTeamColor() != activecColor) {
            throw new InvalidMoveException("Piece at start is opponents piece");
        }

        if(fullBoard.getPiece(end)!=null)
            endPiece=fullBoard.getPiece(end);
        if(fullBoard.getPiece(start)==null){
            throw new InvalidMoveException("Start space is Empty");
        }
        Collection<ChessMove> moves=validMoves(move.getStartPosition());
        if(! moves.contains(new ChessMove(start, end, move.getPromotionPiece()))){
            throw new InvalidMoveException("Not a valid move for the piece");
        }
    }

    private void doPawnStuff(ChessPosition end, ChessMove move){
        if(fullBoard.getPiece(end).getPieceType()== ChessPiece.PieceType.PAWN) {
            if(enPassMove!=null) {

                if (enPassMove.getEndPosition().equals(end)) {
                    fullBoard.addPiece(lastMove.getEndPosition(), new ChessPiece(null, null));
                }
            }
        }

        if(activecColor==TeamColor.WHITE) {
            if (end.getRow() == 8 && fullBoard.getPiece(end).getPieceType() == ChessPiece.PieceType.PAWN) {
                if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) {
                    fullBoard.addPiece(end, new ChessPiece(activecColor, ChessPiece.PieceType.QUEEN));
                } else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) {
                    fullBoard.addPiece(end, new ChessPiece(activecColor, ChessPiece.PieceType.BISHOP));
                } else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) {
                    fullBoard.addPiece(end, new ChessPiece(activecColor, ChessPiece.PieceType.ROOK));
                } else {
                    fullBoard.addPiece(end, new ChessPiece(activecColor, ChessPiece.PieceType.KNIGHT));
                }

            }
        }
        else{
            if (end.getRow() == 1 && fullBoard.getPiece(end).getPieceType() == ChessPiece.PieceType.PAWN) {
                if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) {
                    fullBoard.addPiece(end, new ChessPiece(activecColor, ChessPiece.PieceType.QUEEN));
                } else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) {
                    fullBoard.addPiece(end, new ChessPiece(activecColor, ChessPiece.PieceType.BISHOP));
                } else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) {
                    fullBoard.addPiece(end, new ChessPiece(activecColor, ChessPiece.PieceType.ROOK));
                } else {
                    fullBoard.addPiece(end, new ChessPiece(activecColor, ChessPiece.PieceType.KNIGHT));
                }
            }
        }
    }

    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> allMoves=new HashSet<>(); //moves for the entire side
        Collection<ChessMove> halfMoves=new HashSet<>(); //moves for each piece
        ChessPosition tempPos=new ChessPosition(0, 0);
        ChessPosition kingPos=new ChessPosition(0, 0);

        if(teamColor.equals(TeamColor.WHITE)){
            kingPos=getAllMoves(TeamColor.BLACK, allMoves);

            for(ChessMove move: allMoves){
                if(move.getEndPosition().equals(kingPos)){
                    return true;
                }
            }
        }
        else{
            kingPos=getAllMoves(TeamColor.WHITE, allMoves);
            for(ChessMove move: allMoves){
                if(move.getEndPosition().equals(kingPos)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        Collection<ChessMove> allMoves=new HashSet<>();
        getAllMoves(teamColor, allMoves);

        if(allMoves.isEmpty()){
            return true;
        }

        allMoves=removeInvalidMoves(allMoves, teamColor);
        if(!allMoves.isEmpty()){
            return false;
        }

        return true;

    }

    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        Collection<ChessMove> allMoves=new HashSet<>();
        getAllMoves(teamColor, allMoves);

        if(allMoves.isEmpty()){
            return true;
        }

        allMoves=removeInvalidMoves(allMoves, teamColor);

        if(!allMoves.isEmpty()){
            return false;
        }

        return true;
    }

    private ChessPosition getAllMoves(TeamColor teamColor, Collection<ChessMove> allMoves){
        ChessPosition tempPos;
        Collection<ChessMove> halfMoves;
        ChessPosition kingPos=new ChessPosition(0, 0);
        TeamColor other=TeamColor.WHITE;
        if(teamColor==TeamColor.WHITE){
            other=TeamColor.BLACK;
        }
        for(int i=1; i<9; i++){
            for(int j=1; j<9; j++){
                tempPos=new ChessPosition(i, j);
                if(fullBoard.getPiece(tempPos)!=null) {
                    if (fullBoard.getPiece(tempPos).getTeamColor() == teamColor) {
                        halfMoves = (fullBoard.getPiece(tempPos).pieceMoves(fullBoard, tempPos));
                        allMoves.addAll(halfMoves);
                    }
                }
                if(fullBoard.getPiece(tempPos)!=null) {
                    if (fullBoard.getPiece(tempPos).getTeamColor() == other && fullBoard.getPiece(tempPos).getPieceType() == ChessPiece.PieceType.KING) {
                        kingPos = tempPos;
                    }
                }
            }
        }
        return kingPos;
    }

    private Set<ChessPiece> getWhitePieces(){
        return whitePieces;
    }
    private Set<ChessPiece> getBlackPieces(){
        return blackPieces;
    }



    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        fullBoard=board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return fullBoard;
    }
}
