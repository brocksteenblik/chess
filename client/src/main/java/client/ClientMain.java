package client;

import chess.*;
import ui.Client;

public class ClientMain {
    public static void main(String[] args) {
        var client = new Client("http://localhost:8080");
        client.run();
    }
}
