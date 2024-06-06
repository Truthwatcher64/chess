package ui;

import com.google.gson.Gson;
import websocket.commands.Leave;
import websocket.commands.Resign;

import java.util.Scanner;

public class ChessUI {

    public ChessUI(String authString, int gameNum, WebsocketClient webConnect){
        showMenu();
        new ChessBoardConsole().printBoard();
        this.authString=authString;
        this.webConnect=webConnect;
        this.gameNum=gameNum;


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
            System.out.println("Enter the number for the command");
            System.out.println("[1] Help");
            System.out.println("[2] Logout");
            System.out.println("[3] Create Game");
            System.out.println("[4] List Games");
            System.out.println("[5] Join Game");
            System.out.println("[6] Join Observer");
            System.out.println("\n");
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

    private void help(){
        System.out.println("[1] Help");
        System.out.println("[2] Redraw: Redraw the chess board.");
        System.out.println("[3] Leave: Leave the chess game.");
        System.out.println("[4] Make Move: Make a move on your turn.");
        System.out.println("[5] Resign: Forfeit the game.");
        System.out.println("[6] Highlight: Highlight the possible moves \n\tgiven a starting position");
        System.out.println("\n");
    }

    private void redraw(){

    }

    private void leave(){
        try {
            webConnect.send(new Gson().toJson(new Leave(authString, gameNum)));
            //webConnect.session.close();
            isRunning = false;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private void makeMove(){

    }

    private void resign(){
        try{
            webConnect.send(new Gson().toJson(new Resign(authString, gameNum)));
            //webConnect.session.close();
            isRunning = false;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void highlight(){

    }

}
