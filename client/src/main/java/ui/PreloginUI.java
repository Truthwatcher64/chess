package ui;
import result.LoginResult;
import result.RegisterResult;

import java.util.Map;
import java.util.Scanner;

public class PreloginUI {
    private boolean isRunning;
    private Map<String, String> loggedIn;
    public PreloginUI(){
        isRunning=true;
        loggedIn=null;
        System.out.println("Welcome to Chess!");
        showMenu();
    }
    public void showMenu(){
        String input;
        while(isRunning){
            //Output the options
            System.out.println("Enter the number for the command");
            System.out.println("[1] Help");
            System.out.println("[2] Quit");
            System.out.println("[3] Login");
            System.out.println("[4] Register");
            input=readLine(true, 4);
            if(input != null) {
                System.out.println("\n");
                int num = Integer.parseInt(input);
                switch (num) {
                    case 1 -> help();
                    case 2 -> quit();
                    case 3 -> login();
                    case 4 -> register();
                }
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
        System.out.println("On the menu enter what number for the command you want, and then hit enter.");
        System.out.println("Help: Describes what each command does");
        System.out.println("Quit: Exits the Program.");
        System.out.println("Login: Login as an existing user." +
                "\n\tRequires Username and Password");
        System.out.println("Register: Register yourself as a new user and sign in." +
                "\n\tRequires a unique Username, password, and email.\n");
    }
    private void quit(){
        isRunning=false;
        System.out.println("Have a Good Day");
    }
    private void login(){
        System.out.println("Enter your username");
        String username=readLine(false, -1);
        System.out.println("Enter your password");
        String password=readLine(false, -1);

        try {
            LoginResult auth=new ServerFacade().login(username, password);
            if(auth == null){
                throw new Exception();
            }
            new PostLoginUI(auth.getUsername(), auth.getAuthToken());
            System.out.println("Success\n");
        }
        catch(Exception e){
            System.out.println("Incorrect Username or Password");
            System.out.println(e.getMessage());
            System.out.println("\n");
        }
    }
    private void register(){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Enter your username");
        String username=scanner.nextLine();
        System.out.println("Enter your password");
        String password=scanner.nextLine();
        System.out.println("Enter your email");
        String email=scanner.nextLine();

        try {
            RegisterResult auth=new ServerFacade().register(username, password, email);
            System.out.println("Success");
            System.out.println("\n");
            new PostLoginUI(auth.getUsername(), auth.getAuthToken());
        }
        catch(Exception e){
            System.out.println("User already exists. Try a different Username");
            System.out.println(e.getMessage());
            System.out.println("\n");
        }
    }
}
