package ui;

import chess.*;
import com.google.gson.Gson;
import websocket.commands.Leave;
import websocket.commands.MakeMove;
import websocket.commands.Resign;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class ChessUI extends Endpoint {

    ChessBoardDraw drawer = new ChessBoardDraw();
    ChessGame localCopy = new ChessGame();
    private boolean isRunning;
    private String authString;
    private int gameNum;
    private String color;
    public Session session;

    public ChessUI(String authString, String color, int gameNum) throws Exception{
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.authString=authString;
        this.gameNum=gameNum;
        if(color == null || color.equalsIgnoreCase("white") || color.isBlank()){
            this.color="white";
        }
        else if(color.equalsIgnoreCase("black")){
            this.color="black";
        }

        this.session.addMessageHandler(new MessageHandler.Whole<String>(){
            public void onMessage(String message){
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                switch (serverMessage.getServerMessageType()){
                    case ERROR -> {
                        error(message);
                    }
                    case NOTIFICATION -> {
                        notification(message);
                    }
                    case LOAD_GAME -> {
                        printGame(message);
                    }
                }
            }
        });

    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void send(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }

    private void error(String message){
        Error error = new Gson().fromJson(message, Error.class);
        System.out.println(error.getErrorMessage());
    }

    private void printGame(String message){
        LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
        ChessBoardDraw drawer = new ChessBoardDraw();

        if(loadGame == null || loadGame.getGame() == null){
            System.out.println("Problem loading game. Run redraw");
            return;
        }
        else {
            localCopy = loadGame.getGame();
            drawer.printBoard(localCopy, color, -1, -1);
        }
    }

    private void notification(String message){
        Notification notification = new Gson().fromJson(message, Notification.class);
        System.out.println(notification.getMessage());
    }

    private void help(){
        System.out.println("White pieces: "+ EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.BLACK_QUEEN +" "+EscapeSequences.BLACK_ROOK+" "+EscapeSequences.BLACK_PAWN);
        System.out.println("Black pieces: "+ EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_PAWN +" "+EscapeSequences.BLACK_QUEEN+" "+EscapeSequences.BLACK_BISHOP + EscapeSequences.SET_TEXT_COLOR_WHITE);
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
            input=Integer.parseInt(readLine(true, 7));
            switch (input) {
                case 1 -> help();
                case 2 -> makeMove();
                case 3 -> highlight();
                case 4 -> redraw();
                case 5 -> gameRecord();
                case 6 -> resign();
                case 7 -> leave();
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
        drawer.printBoard(localCopy, color, -1, -1);
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
                send(new Gson().toJson(new Leave(authString, gameNum)));
                isRunning = false;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    private void makeMove(){
        System.out.println("Enter the starting position");
        String temp=readLine(false, -1);
        if(temp.charAt(0)!='a' && temp.charAt(0)!='b' && temp.charAt(0)!='c' && temp.charAt(0)!='d'
                && temp.charAt(0)!='e' && temp.charAt(0)!='f' && temp.charAt(0)!='g' && temp.charAt(0)!='h'){
            System.out.println(temp+" is not a valid square. Enter 2 and try again\n");
            return;
        }
        else if(temp.length() < 2 || (temp.charAt(1)!='1' && temp.charAt(1)!='2' && temp.charAt(1)!='3' && temp.charAt(1)!='4'
                && temp.charAt(1)!='5' && temp.charAt(1)!='6' && temp.charAt(1)!='7' && temp.charAt(1)!='8')){
            System.out.println(temp+" is not a valid square. Enter 2 and try again\n");
            return;
        }

        int tempRow = Integer.parseInt(temp.charAt(1) + "");
        int tempColumn = ((temp.charAt(0) - 96));
        //flip the rows, this is need because in memory the rows are in a different order then they appear on screen
        tempColumn = 9 - tempColumn;
        int startRow=tempColumn;
        int startColumn=tempRow;


        //Get the ending position using same process
        System.out.println("Enter the ending position");
        temp=readLine(false, -1);
        if(temp.charAt(0)!='a' && temp.charAt(0)!='b' && temp.charAt(0)!='c' && temp.charAt(0)!='d'
                && temp.charAt(0)!='e' && temp.charAt(0)!='f' && temp.charAt(0)!='g' && temp.charAt(0)!='h'){
            System.out.println(temp+" is not a valid square. Enter 2 and try again.\n");
            return;
        }
        else if(temp.length()<2 || (temp.charAt(1)!='1' && temp.charAt(1)!='2' && temp.charAt(1)!='3' && temp.charAt(1)!='4'
                && temp.charAt(1)!='5' && temp.charAt(1)!='6' && temp.charAt(1)!='7' && temp.charAt(1)!='8')){
            System.out.println(temp+" is not a valid square. Enter 2 and try again.\n");
            return;
        }

        tempRow = Integer.parseInt(temp.charAt(1) + "");
        tempColumn = ((temp.charAt(0) - 96));
        //flip the rows
        tempColumn = 9- tempColumn;

        int endRow=tempColumn;
        int endColumn=tempRow;


        //Quick Check in case of a promotion
        ChessPiece.PieceType type = null;
        if(endRow==8 && color.equalsIgnoreCase("white")){
            System.out.println("Enter the promotion piece");
            System.out.println("Queen-1\nBishop-2\nKnight-3\nRook-4");
            temp=readLine(true, 4);
            switch (Integer.parseInt(temp)) {
                case 1 -> type = ChessPiece.PieceType.QUEEN;
                case 2 -> type = ChessPiece.PieceType.BISHOP;
                case 3 -> type = ChessPiece.PieceType.KNIGHT;
                case 4 -> type = ChessPiece.PieceType.ROOK;
            }
        }

        if(endRow==1 && color.equalsIgnoreCase("black")){
            System.out.println("Enter the promotion piece");
            System.out.println("Queen-1\nBishop-2\nKnight-3\nRook-4");
            temp=readLine(true, 4);
            switch (Integer.parseInt(temp)) {
                case 1 -> type = ChessPiece.PieceType.QUEEN;
                case 2 -> type = ChessPiece.PieceType.BISHOP;
                case 3 -> type = ChessPiece.PieceType.KNIGHT;
                case 4 -> type = ChessPiece.PieceType.ROOK;
            }
        }

        try {
            ChessMove moveTemp=new ChessMove(new ChessPosition(startColumn, startRow), new ChessPosition(endColumn, endRow), type);
            MakeMove move=new MakeMove(authString, gameNum, moveTemp);
            send(new Gson().toJson(move));
        }
        catch (Exception e){
            System.out.println("Move is invalid: "+e.getMessage());
            System.out.println("Try again. Check valid moves with Highlight Moves.\n");
        }
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
                send(new Gson().toJson(new Resign(authString, gameNum)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void highlight(){
        System.out.println("Enter the starting position");
        String temp=readLine(false, -1);
        if(temp.charAt(0)!='a' && temp.charAt(0)!='b' && temp.charAt(0)!='c' && temp.charAt(0)!='d'
                && temp.charAt(0)!='e' && temp.charAt(0)!='f' && temp.charAt(0)!='g' && temp.charAt(0)!='h'){
            System.out.println(temp+" is not a valid square. Enter 3 and try again");
        }
        else if(temp.charAt(1)!='1' && temp.charAt(1)!='2' && temp.charAt(1)!='3' && temp.charAt(1)!='4'
                && temp.charAt(1)!='5' && temp.charAt(1)!='6' && temp.charAt(1)!='7' && temp.charAt(1)!='8'){
            System.out.println(temp+" is not a valid square. Enter 3 and try again");
        }
        else {
            int tempRow = Integer.parseInt(temp.charAt(1) + "");
            int tempColumn = ((temp.charAt(0) - 96));
            new ChessBoardDraw().printBoard(localCopy, color, tempRow, tempColumn);
        }
    }

    private void gameRecord(){

    }

}
