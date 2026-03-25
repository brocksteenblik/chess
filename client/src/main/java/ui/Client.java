package ui;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import client.*;

import static ui.EscapeSequences.*;

public class Client {

    private State state = State.LOGGED_OUT;
    private final ServerFacade server;
    private String authToken;

    public Client(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public void run(){
        setColor("RESET");
        System.out.print("♕ Welcome to CS 240 Chess!\n");
        System.out.print(help() + "\n");

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")){
            startPrompt();
            String input = scanner.nextLine();
            
            try {
                setColor("BLUE");
                result = eval(input);
                System.out.print(result + "\n");
                optionalPrintHelp(input);
            } catch (Throwable e){
                setColor("RED");
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void optionalPrintHelp(String input) {
        String cmd = input.toLowerCase().split(" ")[0];
        if (cmd.equals("register") || cmd.equals("login") || cmd.equals("logout")){
            System.out.print(help());
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
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> playGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseException error){
            setColor("RED");
            return error.getMessage();
        }
    }

    private String registerUser(String[] params) throws ResponseException {
        assertLoggedOut();
        if (params.length >= 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterResult registerResult = server.userRegistration(username, password, email);
            if (registerResult.username() != null) {
                state = State.LOGGED_IN;
                authToken = registerResult.authToken();
                return String.format("You signed up as %s\n", registerResult.username());
            } else {
                throw new ResponseException(400, "Error: username already taken");
            }
        }
        return "Too few parameters. Make sure to include a username, a password, and an email!";
    }

    private String login(String[] params) throws ResponseException {
    assertLoggedOut();
        if (params.length >= 2) {
            String username = params[0];
            String password = params[1];
            LoginResult loginResult = server.userLogin(username, password);
            if (loginResult.username() != null) {
                state = State.LOGGED_IN;
                authToken = loginResult.authToken();
                return String.format("You logged in as %s\n", loginResult.username());
            } else {
                throw new ResponseException(400, "Error: invalid credentials");
            }
        }
        return "Too few parameters. Make sure to include a username and a password!";
    }


    private String logout() throws ResponseException {
        assertLoggedIn();
        server.userLogout(authToken);
        state = State.LOGGED_OUT;
        authToken = null;
        return "Successfully logged out\n";
    }

    private String createGame(String[] params) throws ResponseException {
        assertLoggedIn();
        if (params.length >= 1){
            String gameName = params[0];
            server.userCreateGame(gameName, authToken);
            return String.format("Successfully created new game: %s", gameName);
        }
        else{
            throw new ResponseException(400, "Error: incorrect number of parameters");
        }
    }

    private String listGames() throws ResponseException {
        assertLoggedIn();
        ArrayList<ListGamesResult> gamesList = getGames();
        int i = 1;
        StringBuilder result = new StringBuilder();
        for (ListGamesResult game : gamesList){
            String gameInfo = String.format("%s\n - WhitePlayer: %s | BlackPlayer: %s",
                    game.gameName(), game.whiteUsername(), game.blackUsername());
            String singleGame = String.format("%d: %s \n", i, gameInfo);
            result.append(singleGame);
            i++;
        }
        return result.toString();
    }

    private ArrayList<ListGamesResult> getGames() throws ResponseException {
        ArrayList<ListGamesResult> gamesList = server.userListGames(authToken);
        return gamesList;
    }

    private String playGame(String[] params) throws ResponseException {
        assertLoggedIn();
        checkValidJoinInput(params);
        int gameIndex = Integer.parseInt(params[0]) - 1;
        int gameID = getGames().get(gameIndex).gameID();
        String color = params[1].toLowerCase();
        server.userPlayGame(gameID, color, authToken);
        String gameName = getGames().get(gameIndex).gameName();
        System.out.printf("Successfully joined game: %s!" + "\n \n", gameName);
        if (color.equals("white")){
            ChessBoard.drawWhitePlayerBoard(gameID);
        }
        else{
            ChessBoard.drawBlackPlayerBoard(gameID);
        }
        return "";
    }

    private void checkValidJoinInput(String[] params) throws ResponseException{
        if (params.length < 2){throw new ResponseException(400, "Error: incorrect number of parameters");}
        if (!params[0].matches("[0-9][0-9]*")){throw new ResponseException(400, "Error: Not a number");}
        int gameID = Integer.parseInt(params[0]);
        if (gameID > getGames().size()){throw new ResponseException(400, "Error: Game not found");}
        String color = params[1].toLowerCase();
        if (!color.equals("white") && !color.equals("black")){throw new ResponseException(400, "Error: invalid player color");}
    }

    private String observeGame(String[] params) throws ResponseException {
        assertLoggedIn();
        checkValidObserveInput(params);
        int gameIndex = Integer.parseInt(params[0]) - 1;
        int gameID = getGames().get(gameIndex).gameID();
        ChessBoard.drawWhitePlayerBoard(gameID);
        return "";
    }

    private void checkValidObserveInput(String[] params) throws ResponseException {
        if (params.length < 1){throw new ResponseException(400, "Error: incorrect number of parameters");}
        if (!params[0].matches("[0-9][0-9]*")){throw new ResponseException(400, "Error: Not a number");}
        int gameID = Integer.parseInt(params[0]);
        if (gameID > getGames().size()){throw new ResponseException(400, "Error: Game not found");}
    }

    private String quit() throws ResponseException {
        String result = "quit";
        if (state == State.LOGGED_IN){
            logout();
        }
        return result;
    }

    private void startPrompt() {
        setColor("RESET");
        System.out.print(">>> ");
    }

    private String help() {
        setColor("BLUE");
        String start = "Please select one of the following:\n";
        System.out.print(start);
        setColor("YELLOW");
        if (state == State.LOGGED_OUT){
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - Provide info to create an account and log in.
                    - ex. register Jim 123456 jim@gmail.com
                    login <USERNAME> <PASSWORD> - Provide info to login to existing account.
                    - ex. login Jim 123456
                    help - Display info about what actions can be taken.
                    quit - Exit the program.
                    """;
        }
        else{
            return """
                    create <NAME> - Create a new game.
                    - ex. create newgame
                    join <ID> [WHITE|BLACK] - Choose a game to play and which color to play as.
                    - ex. join 1 WHITE
                    observe <ID> - Watch an ongoing game.
                    - ex. observe 1
                    list - Show all games in database.
                    logout - Logs current user out.
                    quit - Exit the program.
                    help - Display info about what actions can be taken.
                    """;
        }
    }

    private void assertLoggedOut() throws ResponseException{
        if (state == State.LOGGED_IN){
            setColor("RED");
            throw new ResponseException(401, "Unauthorized: Already logged in");
        }
    }

    private void assertLoggedIn() throws ResponseException{
        if (state == State.LOGGED_OUT){
            setColor("RED");
            throw new ResponseException(401, "Unauthorized: Already logged in");
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
