package ui;

import ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {

    private State state = State.LOGGED_OUT;

    public void run(){
        setColor("RESET");
        System.out.print("Welcome to CS 240 Chess! Please select an option:\n");
        System.out.print(help() + "\n");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            startPrompt();
            String input = scanner.nextLine();
            
            // Remember a Try/Catch block
            result = eval(input);
            setColor("BLUE");
            System.out.print(result + "\n");
        }
    }

    private String eval(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch(cmd){
            case "register" -> registerUser(params);
            case "login" -> login(params);
            case "logout" -> logout(params);
            case "create" -> createGame(params);
            case "list" -> listGames(params);
            case "play" -> playGame(params);
            case "observe" -> observeGame(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String registerUser(String[] params) {
        return null;
    }

    private String login(String[] params) {
        return null;
    }

    private String logout(String[] params) {
        return null;
    }

    private String createGame(String[] params) {
        return null;
    }

    private String listGames(String[] params) {
        return null;
    }

    private String playGame(String[] params) {
        return null;
    }

    private String observeGame(String[] params) {
        return null;
    }

    private void startPrompt() {
        setColor("RESET");
        System.out.print(">>> ");
    }

    private String help() {
        setColor("YELLOW");
        if (state == State.LOGGED_OUT){
            return """
                    help - Display info about what actions can be taken.
                    quit - Exit the program.
                    register <USERNAME> <PASSWORD> <EMAIL> - Provide info to create an account.
                    login <USERNAME> <PASSWORD> - Provide info to login to existing account.
                    """;
        }
        return null;
    }

    private void setColor(String color){
        switch (color) {
            case "GREEN" -> System.out.print(SET_TEXT_COLOR_GREEN);
            case "RED" -> System.out.print(SET_TEXT_COLOR_RED);
            case "BLUE" -> System.out.print(SET_TEXT_COLOR_BLUE);
            case "YELLOW" -> System.out.print(SET_TEXT_COLOR_YELLOW);
            case "WHITE" -> System.out.print(SET_TEXT_COLOR_WHITE);
            case "RESET" -> System.out.print(RESET_TEXT_COLOR);
        }
    }
}
