package handler.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import handler.InputException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.WebSocketService;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final WebSocketService service;

    public WebSocketHandler(DataAccess SqlDataAccess){
        this.service = new WebSocketService(SqlDataAccess);
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
            switch (action.getCommandType()) {
                case CONNECT -> connect(action.getAuthToken(), action.getGameID(), ctx.session);
                case MAKE_MOVE -> makeMove(action.getAuthToken(), action.getGameID(), ctx.session);
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
            connections.add(gameID, session);
            String message = String.format("%s has joined the game", username);
            ChessGame game = service.getGame(gameID);
            ServerMessage rootNotif = new LoadGameMessage(game);
            connections.messageRoot(session, gameID, rootNotif);
            ServerMessage nonRootNotif = new NotificationMessage(message);
            connections.broadcast(session, gameID, nonRootNotif);
        } catch (DataAccessException e) {
            ServerMessage rootError = new ErrorMessage("Error: Faulty Data Provided");
            connections.messageRoot(session, gameID, rootError);
        }
    }

    private void makeMove(String authToken, int gameID, Session session) {

    }

    private void leave(String authToken, int gameID, Session session) {
    }

    private void resign(String authToken, int gameID, Session session) {
    }


    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        System.out.println("Websocket closed");
    }
}
