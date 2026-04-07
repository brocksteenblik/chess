package client;

import com.google.gson.Gson;
import io.javalin.http.Handler;
import io.javalin.http.HandlerType;
import jakarta.websocket.*;
import io.javalin.router.EndpointMetadata;
import org.jetbrains.annotations.NotNull;
import ui.Client;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

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
                    client.notify(notification);
                }
            });
        } catch (URISyntaxException | DeploymentException | IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(jakarta.websocket.Session session, EndpointConfig endpointConfig) {
    }
}
