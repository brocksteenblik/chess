package handler.websocket;

import com.google.gson.Gson;
import handler.InputException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        try {
            UserGameCommand action = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (action.getCommandType()) {
                case CONNECT -> connect(action.getAuthToken(), ctx.session);
                case MAKE_MOVE -> makeMove(action.getAuthToken(), ctx.session);
                case LEAVE -> leave(action.getAuthToken(), ctx.session);
                case RESIGN -> resign(action.getAuthToken(), ctx.session);
            }
        } catch (InputException ex) { // change exception type later
            ex.printStackTrace();
        }
    }

    private void connect(String authToken, Session session) {

    }

    private void makeMove(String authToken, Session session) {

    }

    private void leave(String authToken, Session session) {
    }

    private void resign(String authToken, Session session) {
    }


    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        System.out.println("Websocket closed");
    }
}
