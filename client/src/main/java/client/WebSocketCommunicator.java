package client;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import ui.Client;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketCommunicator extends Endpoint {

    Session session;
    Client client;

    public WebSocketCommunicator(String url, Client client) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI uri = new URI(url + "/ws");
            this.client = client;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                    if (notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                        LoadGameMessage realNotification = new Gson().fromJson(message, LoadGameMessage.class);
                        client.notify(realNotification);
                    }
                    else if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        NotificationMessage realNotification = new Gson().fromJson(message, NotificationMessage.class);
                        client.notify(realNotification);
                    }
                    else {
                        ErrorMessage realNotification = new Gson().fromJson(message, ErrorMessage.class);
                        client.notify(realNotification);
                    }
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void joinGame(String authToken, int gameID) throws ResponseException {
        try {
            UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e) {
            throw new ResponseException(500, "Could not connect");
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            MakeMoveCommand makeMoveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            session.getBasicRemote().sendText(new Gson().toJson(makeMoveCommand));
        } catch (IOException e) {
            throw new ResponseException(500, "Could not connect");
        }
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException {
        try {
            UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e) {
            throw new ResponseException(500, "Could not connect to server");
        }
    }

    @Override
    public void onOpen(jakarta.websocket.Session session, EndpointConfig endpointConfig) {
    }

    public void resignPlayer(String authToken, int gameID) throws ResponseException {
        try {
            UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException e) {
            throw new ResponseException(500, "Could not connect to server");
        }
    }
}
