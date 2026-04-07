package client;

import chess.*;
import ui.Client;

public class ClientMain {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1){
            serverUrl = args[0];
        }
        Client client = null;
        try {
            client = new Client(serverUrl);
        } catch (ResponseException e) {
            System.out.print("Unable to start server");
        }
        client.run();
    }
}
