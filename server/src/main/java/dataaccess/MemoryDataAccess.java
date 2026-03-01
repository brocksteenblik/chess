package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess {

    final private HashMap<String, UserData> users = new HashMap<>();
    final private HashMap<String, AuthData> auths = new HashMap<>();
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void createUser(UserData userData){
        users.put(userData.username(), userData);
    }

    public AuthData createAuth(String authToken, String username){
        authToken = UUID.randomUUID().toString();
        AuthData authData =  new AuthData(authToken, username);
        auths.put(authToken, authData);
        return authData;
    }

    public AuthData getAuth(String authToken){return auths.get(authToken);}

    public void deleteAuth(AuthData authData) {
        auths.remove(authData.authToken());
    }

    public ListGamesResult listGames(){
        return new ListGamesResult(games.values());
    }

    public int createGame(String gameName){
        Random random = new Random();
        int gameID = random.nextInt(10000);
        GameData game = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, game);
        return gameID;
    }

    public void updateGame(AuthData authData, JoinGameRequest joinGameRequest){
        String username = authData.username();
        Integer gameID = joinGameRequest.gameID();
        GameData gameData = games.get(gameID);
        if (joinGameRequest.playerColor() == JoinGameRequest.PlayerColor.WHITE){
            GameData newGameData = gameData.setWhiteUsername(username);
            games.put(newGameData.gameID(), newGameData);
        }
        else if (joinGameRequest.playerColor() == JoinGameRequest.PlayerColor.BLACK){
            GameData newGameData = gameData.setBlackUsername(username);
            games.put(newGameData.gameID(), newGameData);
        }
    }

    public void clear(){
        users.clear();
        auths.clear();
        games.clear();
    }
}
