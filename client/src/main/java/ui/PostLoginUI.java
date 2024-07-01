package ui;

import com.google.gson.Gson;
import model.GameData;
import websocket.commands.Connect;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostLoginUI {
    private boolean isRunning;
    private String username;
    private String authString;
    private ArrayList<GameData> localGames;
    public PostLoginUI(String username, String authString) {
        this.username=username;
        this.authString=authString;
        showMenu();
    }

    public void showMenu(){
        isRunning=true;
        int input;
        while(isRunning){
            //Output the options
            System.out.println("Signed in as "+username);
            System.out.println("Enter the number for the command");
            System.out.println("[1] Help");
            System.out.println("[2] Logout");
            System.out.println("[3] Create Game");
            System.out.println("[4] List Games");
            System.out.println("[5] Join Game");
            System.out.println("[6] Join Observer");
            System.out.println();
            input=Integer.parseInt(readLine(true, 6));
            switch (input) {
                case 1 -> help();
                case 2 -> logout();
                case 3 -> createGame();
                case 4 -> listGame();
                case 5 -> joinGame();
                case 6 -> joinObserver();
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

    //help, logout, create game, list games, join game, join observer
    private void help(){
        System.out.println("Signed in as "+username);
        System.out.println("[1] Help");
        System.out.println("[2] Logout: Sign Out and return to the start menu.");
        System.out.println("[3] Create Game: Create a new game to play." +
                "\n\t- Requires a unique game name.");
        System.out.println("[4] List Games: List all the games that are active.");
        System.out.println("[5] Join Game: Join a game by ID number." +
                "\n\t- Enter the game ID and color you want to join as." +
                "\n\t- To see the Game IDs run List Games");
        System.out.println("[6] Join Observer: Join a game to watch two other players." +
                "\n\t- Enter the game ID to join.");
        System.out.println("\n");
    }
    private void logout() {
        try{
            new ServerFacade().logout(authString);
            isRunning=false;
        }
        catch (Exception e){
            System.out.println("Error Logging out: "+e.getMessage());
        }
    }
    private void createGame(){
        try {
            System.out.println("Enter the game name:");
            String name = readLine(false, -1);
            if (name == null) {
                throw new Exception("No name was entered");
            }
            new ServerFacade().createGame(authString, name);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void listGame() {
        //list games no return
        try {
            updateLocalGames();
            if(localGames == null || localGames.isEmpty()){
                System.out.println("No Games Created");
            }
            else{
                int count = 1;
                for(GameData game : localGames){
                    System.out.println("["+count+"] "+game.gameName() + " - White: " + game.whiteUsername()+", Black: "+ game.blackUsername());
                    count++;
                }
            }
            System.out.println();

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void joinGame(){
        try {
            updateLocalGames();
            System.out.println("Enter the Number for the Game or -1 to return to the menu:");
            int gameNum = Integer.parseInt(readLine(true, 9999));
            if(gameNum == -1){
                return;
            }
            gameNum--;
            System.out.println("Enter what color you will play as:");
            String color = readLine(false, -1);
            if(color != null && !color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black" )){
                throw new Exception();
            }
            int realGameID = localGames.get(gameNum).gameID();

            //Changes the database
            new ServerFacade().joinGame(authString, realGameID, username, color);

            //Prints out the chessboard and runs the actual game
            ChessUI client = new ChessUI(authString, color, realGameID);
            client.send(new Gson().toJson(new Connect(authString, realGameID)));

        }
        catch(Exception e){
            System.out.println("Game number was incorrect or the color was not 'black' or 'white'.");
            System.out.println("There may also already be a player, check this with List Games.");
            System.out.println("\n");
        }


    }
    private void joinObserver(){
        try {
            System.out.println("Enter the Number for the Game or -1 to return to the menu:");
            int gameNum = Integer.parseInt(readLine(true, 9999));
            gameNum--;
            updateLocalGames();
            int realGameID = localGames.get(gameNum).gameID();
            System.out.println("Enter what color you will play as:");
            String color = null;

            ChessUI client = new ChessUI(authString, null, realGameID);
            client.send(new Gson().toJson(new Connect(authString, realGameID)));


        }
        catch(Exception e){
            System.out.println("Game number was incorrect.");
            System.out.println(e.getMessage());
            System.out.println("\n");
        }
    }

    private void updateLocalGames(){
        try {
            localGames = (ArrayList<GameData>) new ServerFacade().listGames(authString);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
