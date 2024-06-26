package ui;

import com.google.gson.Gson;
import websocket.commands.Leave;
import websocket.commands.Resign;

import java.util.Scanner;

public class ChessUI {

    public ChessUI(String authString, int gameNum, WebsocketClient webConnect){
        showMenu();
        //new ChessBoardConsole().printBoard();
        this.authString=authString;
        this.webConnect=webConnect;
        this.gameNum=gameNum;


    }

    private void help(){
        System.out.println("White pieces: "+EscapeSequences.WHITE_KING +" "+EscapeSequences.WHITE_ROOK+" "+EscapeSequences.WHITE_PAWN);
        System.out.println("Black pieces: "+EscapeSequences.BLACK_PAWN +" "+EscapeSequences.BLACK_QUEEN+" "+EscapeSequences.BLACK_BISHOP);
        System.out.println("[1] Help: Prints the help menu." );
        System.out.println("[2] Make Move: Enter your move with a starting position and ending position.\n" +
                "\tExample g1 f3 or e2 e4. Use only lower case letters.");
        System.out.println("[3] Highlight Moves: Highlights all the possible moves from a starting position.\n" +
                "\tStarting position is the format column/row. Example: e3 or f5. Use only lower case letters.");
        System.out.println("[4] Redraw: Refreshes the board. Will remove any highlighted squares.\n" +
                "\tIf the board has a weird issue print run this command to reset it.");
        System.out.println("[5] Print record of all game moves.\n" +
                "\tThis feature is not not yet implemented");
        System.out.println("[6] Resign: Resign the game as a loss.");
        System.out.println("[7] Leave: Leave the game. You will also be removed as the current player in the game \n" +
                "\tand another person can take your spot.");
    }

    private boolean isRunning;
    private String authString;
    private WebsocketClient webConnect;
    private int gameNum;

    public void showMenu(){
        isRunning=true;
        int input;
        while(isRunning){
            //Output the options
            System.out.println("Enter an option");
            System.out.println("[1] Help");
            System.out.println("[2] Make Move");
            System.out.println("[3] Highlight Moves");
            System.out.println("[4] Redraw");
            System.out.println("[5] Game Record");
            System.out.println("[6] Resign");
            System.out.println("[7] Leave\n");
            input=Integer.parseInt(readLine(true, 6));
            switch (input) {
                case 1 -> help();
                case 2 -> redraw();
                case 3 -> leave();
                case 4 -> makeMove();
                case 5 -> resign();
                case 6 -> highlight();
            }
        }
    }

    private String readLine(boolean numCheck, int max){
        Scanner scanner=new Scanner(System.in);
        String input=scanner.nextLine();
        if(numCheck){
            try {
                int num = Integer.parseInt(input);
                if(num<0 || num>max){
                    throw new NumberFormatException();
                }
                return input;

            } catch (NumberFormatException e) {
                System.out.println(input + " is not a valid option. " +
                        "\nPlease enter an option again.\n");
            }
        }
        else{
            return input;
        }

        return null;
    }

    private void redraw(){

    }

    private void leave(){
        System.out.println("Are you sure you want to exit? [y/n]");
        String temp=readLine(false, -1);
        while(!temp.equalsIgnoreCase("y") && !temp.equalsIgnoreCase("n")){
            System.out.println("Enter 'y' for yes, and 'n' for no");
            temp=readLine(false, -1);
        }
        if(temp.equalsIgnoreCase("y")) {
            try {
                webConnect.send(new Gson().toJson(new Leave(authString, gameNum)));
                isRunning = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void makeMove(){

    }

    private void resign(){
        System.out.println("Are you sure you want to resign? [y/n]");
        String temp=readLine(false, -1);
        while(!temp.equalsIgnoreCase("y") && !temp.equalsIgnoreCase("n")){
            System.out.println("Enter 'y' for yes, and 'n' for no");
            temp=readLine(false, -1);
        }
        if(temp.equalsIgnoreCase("y")) {
            try {
                webConnect.send(new Gson().toJson(new Resign(authString, gameNum)));
                isRunning = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void highlight(){

    }

}
