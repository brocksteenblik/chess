package service;

import com.google.gson.Gson;

import java.util.Map;

public class AlreadyTaken extends RuntimeException {
    final private int code;

    public AlreadyTaken(int code, String message) {
        super(message);
        this.code = code;
    }
    public int getCode(){return code;}

    public String toJson(){return new Gson().toJson(Map.of("message", getMessage(), "status", code));}
}
