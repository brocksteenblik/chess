package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import client.*;
import org.jetbrains.annotations.NotNull;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import static ui.EscapeSequences.*;

public class Client {

    private State state = State.LOGGED_OUT;
    private final ServerFacade server;
    private String authToken;
    private WebSocketCommunicator ws;
    private int gameID = 0;
    private chess.ChessPosition chessPosition;
    private ChessGame game;

    public Client(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
        ws = new WebSocketCommunicator(serverUrl, this);
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

    public void notify(LoadGameMessage notification) {
        GameData gameData = notification.getGame();
        this.game = gameData.getChessGame();
        System.out.print("\n");
        if (state == State.BLACK_PLAYER){
            ChessBoard.drawBlackPlayerBoard(game, chessPosition);
        }
        else{
            ChessBoard.drawWhitePlayerBoard(game, chessPosition);
        }
        startPrompt();
    }

    public void notify(NotificationMessage notification) {
        setColor("BLUE");
        System.out.println(notification.getMessage());
        startPrompt();
    }


    public void notify(ErrorMessage notification) {
        System.out.println(notification.getErrorMessage());
        startPrompt();
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
                case "redraw" -> redrawBoard(params);
                case "move" -> movePiece(params);
                case "resign" -> resignPlayer(params);
                case "highlight" -> highlightMoves(params);
                case "leave" -> leaveGame(params);
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
        if (params.length == 3) {
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
        return "Incorrect number of parameters. Make sure to include a username, a password, and an email!\n";
    }

    private String login(String[] params) throws ResponseException {
    assertLoggedOut();
        if (params.length == 2) {
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
        return "Incorrect number of parameters. Make sure to include a username and a password!\n";
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
        if (params.length == 1){
            String gameName = params[0];
            server.userCreateGame(gameName, authToken);
            return String.format("Successfully created new game: %s", gameName);
        }
        else{
            throw new ResponseException(400, "Error: incorrect number of parameter\n");
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
        if (color.equals("white")){
            state = State.WHITE_PLAYER;
        }
        else{
            state = State.BLACK_PLAYER;
        }
        ws.joinGame(authToken, gameID);
        server.userPlayGame(gameID, color, authToken);
        String gameName = getGames().get(gameIndex).gameName();
        this.gameID = gameID;
        return String.format("Successfully joined game %s!" + "\n \n", gameName);
    }

    private void checkValidJoinInput(String[] params) throws ResponseException{
        if (params.length != 2){throw new ResponseException(400, "Error: incorrect number of parameters");}
        if (!params[0].matches("[0-9][0-9]*")){throw new ResponseException(400, "Error: Not a valid number");}
        int gameID = Integer.parseInt(params[0]);
        if (gameID > getGames().size()){throw new ResponseException(400, "Error: Game not found");}
        String color = params[1].toLowerCase();
        if (!color.equals("white") && !color.equals("black")){throw new ResponseException(400, "Error: invalid player color");}
    }

    private String observeGame(String[] params) throws ResponseException {
        assertLoggedIn();
        checkValidObserveInput(params);
        int gameIndex = Integer.parseInt(params[0]) - 1;
        // debug both server and client
        int gameID = getGames().get(gameIndex).gameID();
        ws.joinGame(authToken, gameID);
        this.gameID = gameID;
        state = State.OBSERVER;
        return "";
    }

    private void checkValidObserveInput(String[] params) throws ResponseException {
        if (params.length != 1){throw new ResponseException(400, "Error: incorrect number of parameters");}
        if (!params[0].matches("[0-9][0-9]*")){throw new ResponseException(400, "Error: Not a number");}
        int gameID = Integer.parseInt(params[0]);
        if (gameID > getGames().size()){throw new ResponseException(400, "Error: Game not found");}
    }


    private String redrawBoard(String[] params) throws ResponseException {
        assertInGame();
        if (state.equals(State.BLACK_PLAYER)){
            ui.ChessBoard.drawBlackPlayerBoard(game, null);
        }
        else{
            ui.ChessBoard.drawWhitePlayerBoard(game, null);
        }
        return "";
    }

    private String movePiece(String[] params)throws ResponseException {
        assertPlayingGame();
        assertValidMovePositions(params);
        int rowNum = Integer.parseInt(params[0].substring(0,1));
        String colLetter = params[0].substring(1);
        int colNum = Integer.parseInt(convertLetterToNum(colLetter));
        ChessPosition start;
        if (state == State.WHITE_PLAYER){
            start = new ChessPosition(rowNum, colNum);
        }
        else{
            start = new ChessPosition(rowNum, 9 - colNum);
        }
        rowNum = Integer.parseInt(params[1].substring(0,1));
        colLetter = params[1].substring(1);
        colNum = Integer.parseInt(convertLetterToNum(colLetter));
        ChessPosition end;
        if (state == State.WHITE_PLAYER){
            end = new ChessPosition(rowNum, colNum);
        }
        else{
            end = new ChessPosition(rowNum, 9 - colNum);
        }
        // work on promotionPieces later
        ChessMove move = new ChessMove(start, end, null);
        ws.makeMove(authToken, gameID, move);
        return "";
    }

    private void assertValidMovePositions(String[] params) throws ResponseException{
        if (params.length != 2){throw new ResponseException(400, "Error: incorrect number of parameters");}
        if (!params[0].matches("[0-8][abcdefgh]")){throw new ResponseException(400, "Error: Invalid position provided");}
        if (!params[1].matches("[0-8][abcdefgh]")){throw new ResponseException(400, "Error: Invalid position provided");}
    }

    private String resignPlayer(String[] params) throws ResponseException {
        assertPlayingGame();
        // Add menu dialogue to confirm resign
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you SURE you want to resign? y/n\n");
        startPrompt();
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("y")) {
            ws.resignPlayer(authToken, gameID);
            return "Successfully resigned. You'll get them next time!";
        } else if (input.equalsIgnoreCase("n")) {
            return "";
        } else {throw new ResponseException(400, "Improper response");}
    }

    private String highlightMoves(String[] params) throws ResponseException {
        assertInGame();
        assertValidHighlightPosition(params);
        int rowNum = Integer.parseInt(params[0].substring(0,1));
        String colLetter = params[0].substring(1);
        int colNum = Integer.parseInt(convertLetterToNumHighlight(colLetter));
        if (state.equals(State.BLACK_PLAYER)){
            chessPosition = new ChessPosition(rowNum, colNum);
            ui.ChessBoard.drawBlackPlayerBoard(game, chessPosition);
        }
        else{
            chessPosition = new ChessPosition(rowNum, 9 - colNum);
            ui.ChessBoard.drawWhitePlayerBoard(game, chessPosition);
        }
        chessPosition = null;
        return "";
    }

    private void assertValidHighlightPosition(String[] params) throws ResponseException{
        if (params.length != 1){throw new ResponseException(400, "Error: incorrect number of parameters");}
        if (!params[0].matches("[0-8][abcdefgh]")){throw new ResponseException(400, "Error: Invalid position provided");}
    }

    private String convertLetterToNum(String col) throws ResponseException{
        if (state == State.WHITE_PLAYER) {
            return alphabeticalOrder(col);
        } else if (state == State.BLACK_PLAYER) {
            return inverseAlphabeticalOrder(col);
        } else {return "0";}
    }

    @NotNull
    private static String inverseAlphabeticalOrder(String col) throws ResponseException {
        return switch (col) {
            case "a" -> "8";
            case "b" -> "7";
            case "c" -> "6";
            case "d" -> "5";
            case "e" -> "4";
            case "f" -> "3";
            case "g" -> "2";
            case "h" -> "1";
            default -> throw new ResponseException(400, "Invalid column provided");
        };
    }

    @NotNull
    private static String alphabeticalOrder(String col) throws ResponseException {
        return switch (col) {
            case "a" -> "1";
            case "b" -> "2";
            case "c" -> "3";
            case "d" -> "4";
            case "e" -> "5";
            case "f" -> "6";
            case "g" -> "7";
            case "h" -> "8";
            default -> throw new ResponseException(400, "Invalid column provided");
        };
    }

    private String convertLetterToNumHighlight(String col) throws ResponseException{
        if (state == State.BLACK_PLAYER) {
            return alphabeticalOrder(col);
        } else {
            return inverseAlphabeticalOrder(col);
        }
    }


    private String leaveGame(String[] params) throws ResponseException {
        assertInGame();
        ws.leaveGame(authToken, gameID);
        gameID = 0;
        game = null;
        state = State.LOGGED_IN;
        return "Successfully left game! Type command 'help' for options.";
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
        else if (state == State.LOGGED_IN){
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
        else if (state == State.OBSERVER){
            return """
                    redraw - Prints the chess board again.
                    highlight <row> <column> - Highlights legal moves for a piece.
                    leave - Leave the game without ending it.
                    help - Display info about what actions can be taken.
                    """;
        }
        else{
            return """
                    redraw - Prints the chess board again.
                    move <startPosition> <endPosition> <Promotion (optional)> - Make a move in current game.
                    - ex. move 2d 3d
                    - ADD PROMOTION STUFF LATER
                    resign - Forfeit and end the current game.
                    highlight <piecePosition> - Highlights legal moves for a piece.
                    - ex. highlight 2d
                    leave - Leave the game without ending it.
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
            throw new ResponseException(401, "Unauthorized: Not logged in");
        }
    }

    private void assertInGame() throws ResponseException{
        if (state != State.WHITE_PLAYER && state != State.BLACK_PLAYER && state != State.OBSERVER){
            setColor("RED");
            throw new ResponseException(401, "Unauthorized: Not in a game");
        }
    }

    private void assertPlayingGame() throws ResponseException{
        if (state != State.WHITE_PLAYER && state != State.BLACK_PLAYER){
            setColor("RED");
            throw new ResponseException(401, "Unauthorized: Not playing a game");
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
