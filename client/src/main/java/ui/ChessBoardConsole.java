package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

import static ui.EscapeSequences.*;

public class ChessBoardConsole {
    public static int position=0;
    public static Vector<String> pieces=basicBoard();

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        pieces=basicBoard();
        System.out.print(SET_BG_COLOR_PURPLE+" A "+RESET_BG_COLOR+"\n");
        System.out.print(SET_BG_COLOR_BLUE+ " A "+RESET_BG_COLOR+"\n");


        printEmptyBoard();
        System.out.println();
        pieces=basicBoard();
        pieces=reversePieces(pieces);
        position=0;
        printEmptyBoard();
    }
    public void printBoard(){
        printEmptyBoard();
        System.out.println();
        pieces=basicBoard();
        pieces=reversePieces(pieces);
        position=0;
        printEmptyBoard();
    }
    public static Vector<String> basicBoard(){
        Vector<String> pieces=new Vector<>();
        //pieces.add(EMPTY);
        pieces.add(WHITE_ROOK);
        pieces.add(WHITE_KNIGHT);
        pieces.add(WHITE_BISHOP);
        pieces.add(WHITE_QUEEN);
        pieces.add(WHITE_KING);
        pieces.add(WHITE_BISHOP);
        pieces.add(WHITE_KNIGHT);
        pieces.add(WHITE_ROOK);
        pieces.add(WHITE_PAWN);
        pieces.add(WHITE_PAWN);
        pieces.add(WHITE_PAWN);
        pieces.add(WHITE_PAWN);
        pieces.add(WHITE_PAWN);
        pieces.add(WHITE_PAWN);
        pieces.add(WHITE_PAWN);
        pieces.add(WHITE_PAWN);
        for(int i=0; i<32; i++){
            pieces.add(EMPTY);
        }
        pieces.add(BLACK_PAWN);
        pieces.add(BLACK_PAWN);
        pieces.add(BLACK_PAWN);
        pieces.add(BLACK_PAWN);
        pieces.add(BLACK_PAWN);
        pieces.add(BLACK_PAWN);
        pieces.add(BLACK_PAWN);
        pieces.add(BLACK_PAWN);
        pieces.add(BLACK_ROOK);
        pieces.add(BLACK_KNIGHT);
        pieces.add(BLACK_BISHOP);
        pieces.add(BLACK_QUEEN);
        pieces.add(BLACK_KING);
        pieces.add(BLACK_BISHOP);
        pieces.add(BLACK_KNIGHT);
        pieces.add(BLACK_ROOK);
        return pieces;
    }

    public static Vector<String> reversePieces(Vector<String> pieces){
        Vector<String> temp=new Vector<>();
        for(int i=pieces.size()-1; i>=0; i--){
            temp.add(pieces.get(i));
        }
        return temp;
    }

    public static void printEmptyBoard(){
        boolean square=true;

        printEdge();

        for(int i=0; i<8; i++){
            if(i%2 == 0){
                printRow(square, i);
            }
            else{
                printRow(!square, i);
            }
        }

        printEdge();


    }
    public static void printEdge(){
        //edge
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0; i<10; i++){
            if(i>0 && i<9){
                System.out.print(SET_BG_COLOR_BLACK+ ui.EscapeSequences.SET_TEXT_COLOR_WHITE+" "+(char)(64+i)+" ");
            }
            else if(i==0){
                System.out.print(SET_BG_COLOR_BLACK+ EMPTY+ "");
            }
            else{
                System.out.print(SET_BG_COLOR_BLACK+ EMPTY+ "");
            }

        }
        System.out.print(ui.EscapeSequences.RESET_BG_COLOR+"\n");
    }
    public static boolean printRow(boolean square, int row){
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //one row
        System.out.print(ui.EscapeSequences.SET_BG_COLOR_BLACK+ " "+(char)(49+row)+" ");
        for(int i=0; i<8; i++){
            if(square){
                System.out.print(SET_BG_COLOR_LIGHT_GREY+getPiece(position));
                square=false;
            }
            else{
                System.out.print(SET_BG_COLOR_DARK_GREY+getPiece(position));
                square=true;
            }

        }
        System.out.print(SET_BG_COLOR_BLACK+" "+ (char)(49+row)+" ");
        System.out.print(EscapeSequences.RESET_BG_COLOR+"\n");
        return square;
    }

    public static String getPiece(int pos){
        position++;
        return pieces.get(pos);

    }
}