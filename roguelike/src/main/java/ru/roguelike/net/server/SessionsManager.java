package ru.roguelike.net.server;

import com.sun.org.glassfish.gmbal.GmbalException;
import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.ServerReply;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.GameModelImpl;
import ru.roguelike.logic.generators.RandomGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SessionsManager {
    // sessionID -> game
    private HashMap<String, GameModel> games = new HashMap<>();
    // sessionID -> game clients
    private HashMap<String, HashSet<StreamObserver<ServerReply>>> clientsMapper = new HashMap<>();

    public void addClientToGame(@NotNull String sessionId, StreamObserver<ServerReply> client) {
        HashSet<StreamObserver<ServerReply>> currentClients = clientsMapper.get(sessionId);

        if (currentClients == null) {
            currentClients = new HashSet<>();
        }

        currentClients.add(client);
        clientsMapper.put(sessionId, currentClients);
    }

    public HashSet<StreamObserver<ServerReply>> getGameClients(@NotNull String sessionId) {
        return clientsMapper.getOrDefault(sessionId, new HashSet<>());
    }

    public void removeClient(@NotNull String sessionId, StreamObserver<ServerReply> client) {
        HashSet<StreamObserver<ServerReply>> currentClients = clientsMapper.get(sessionId);

        if (currentClients == null) {
            return;
        }

        currentClients.remove(client);
        clientsMapper.put(sessionId, currentClients);
    }

    public GameModel getGameById(@NotNull String sessionId) {
        return games.get(sessionId);
    }

    public void setGameById(@NotNull String sessionId, @NotNull GameModel gameModel) {
        games.put(sessionId, gameModel);
    }

    public Set<String> getAllGames() {
        return  games.keySet();
    }
}
