package client;

public class ResponseException extends Exception {

    final private int code;

    public ResponseException(int code, String message) {
        super(message);
        this.code = code;
    }
}
