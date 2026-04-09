package handler.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.WebSocketService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final WebSocketService service;

    public WebSocketHandler(DataAccess sqlDataAccess){
        this.service = new WebSocketService(sqlDataAccess);
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        try {
            UserGameCommand action = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            ChessMove move = null;
            if (action.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
                move = new Gson().fromJson(ctx.message(), MakeMoveCommand.class).getMove();
            }
            switch (action.getCommandType()) {
                case CONNECT -> connect(action.getAuthToken(), action.getGameID(), ctx.session);
                case MAKE_MOVE -> makeMove(action.getAuthToken(), action.getGameID(), move, ctx.session);
                case LEAVE -> leave(action.getAuthToken(), action.getGameID(), ctx.session);
                case RESIGN -> resign(action.getAuthToken(), action.getGameID(), ctx.session);
            }
        } catch (Exception ex) { // change exception type later
            ex.printStackTrace();
        }
    }

    private void connect(String authToken, int gameID, Session session) throws IOException, DataAccessException {
        try {
            String username = service.getUsernameFromAuth(authToken);
            GameData game = service.getGame(gameID);
            connections.add(gameID, session);
            ServerMessage rootNotif = new LoadGameMessage(game);
            connections.messageRoot(session, gameID, rootNotif);
            String message = String.format("%s has joined the game", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(session, gameID, notificationMessage);
        } catch (DataAccessException e) {
            ServerMessage rootError = new ErrorMessage("Error: Faulty Data Provided");
            connections.messageRoot(session, gameID, rootError);
        }
    }

    private void makeMove(String authToken, int gameID, ChessMove move, Session session) throws IOException {
        try {
            if (!service.checkValidMove(authToken, gameID, move)){
                ServerMessage rootError = new ErrorMessage("Error: Invalid move");
                connections.messageRoot(session, gameID, rootError);
            }

        } catch (DataAccessException e) {
            ServerMessage rootError = new ErrorMessage("Error: Faulty Data Provided");
            connections.messageRoot(session, gameID, rootError);
        }
    }

    private void leave(String authToken, int gameID, Session session) throws IOException {
        try {
            service.checkValidAuth(authToken);
            service.checkValidGameID(gameID);
            connections.remove(session, gameID);
            String username = service.getUsernameFromAuth(authToken);
            if (service.checkIfPlayer(username, gameID)){
                service.removePlayerFromGame(authToken, gameID);
            }
            String message = String.format("%s has left the game", username);
            ServerMessage notificationMessage = new NotificationMessage(message);
            connections.broadcast(session, gameID, notificationMessage);
        } catch (DataAccessException e) {
            ServerMessage rootError = new ErrorMessage("Error: Faulty Data Provided");
            connections.messageRoot(session, gameID, rootError);
        }
    }

    private void resign(String authToken, int gameID, Session session) {
    }


    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        System.out.println("Websocket closed");
    }
}
