package handler;

import com.google.gson.Gson;
import java.util.Map;

public class InputException extends RuntimeException {

    final private int code;

    public InputException(int code, String message) {
        super(message);
        this.code = code;
    }
    public int getCode(){return code;}

    public String toJson(){return new Gson().toJson(Map.of("message", getMessage(), "status", code));}
}
