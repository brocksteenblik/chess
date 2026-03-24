package ui;

import model.RegisterResult;

import java.util.Arrays;
import java.util.Scanner;
import client.*;

import static ui.EscapeSequences.*;

public class Client {

    private State state = State.LOGGED_OUT;
    private final ServerFacade server;

    public Client(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void run(){
        setColor("RESET");
        System.out.print("♕ Welcome to CS 240 Chess! Please select an option:\n");
        System.out.print(help() + "\n");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            startPrompt();
            String input = scanner.nextLine();
            
            try {
                result = eval(input);
                setColor("BLUE");
                System.out.print(result + "\n");
            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
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
        } catch (ResponseException error){
            return error.getMessage();
        }
    }

    private String registerUser(String[] params) throws ResponseException {
        if (params.length >= 3){
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterResult registerResult = server.userRegistration(username, password, email);
            if (registerResult.username() != null){
                state = State.LOGGED_IN;
                return String.format("You signed up as %s", registerResult.username());
            }
            else{
                return "That username is already taken. Try a new username!";
            }
        }
        return "Too few parameters. Make sure to include a username, a password, and an email!";
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
                    register <USERNAME> <PASSWORD> <EMAIL> - Provide info to create an account.
                    login <USERNAME> <PASSWORD> - Provide info to login to existing account.
                    help - Display info about what actions can be taken.
                    quit - Exit the program.
                    """;
        }
        else{
            return """
                    create <NAME> - Create a new game.
                    list - Show all games in database.
                    join <ID> [WHITE|BLACK] - Choose a game to play and which color to play as.
                    observe <ID> - Watch an ongoing game.
                    logout - Logs current user out.
                    quit - Exit the program.
                    help - Display info about what actions can be taken.
                    """;
        }
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
