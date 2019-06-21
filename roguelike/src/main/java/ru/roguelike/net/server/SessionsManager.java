package ru.roguelike.net.server;

import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.ServerReply;
import ru.roguelike.logic.GameModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Manager of server sessions
 */
public class SessionsManager {
    // sessionID -> game
    private HashMap<String, GameModel> games = new HashMap<>();
    // sessionID -> game clients
    private HashMap<String, HashSet<StreamObserver<ServerReply>>> clientsMapper = new HashMap<>();

    /**
     * Add client to list of clients of current game
     * @param sessionId session to add client
     * @param client client stream
     */
    public void addClientToGame(@NotNull String sessionId, StreamObserver<ServerReply> client) {
        HashSet<StreamObserver<ServerReply>> currentClients = clientsMapper.get(sessionId);

        if (currentClients == null) {
            currentClients = new HashSet<>();
        }

        currentClients.add(client);
        clientsMapper.put(sessionId, currentClients);
    }

    /**
     * Get list of game clients
     * @param sessionId session id
     */
    public HashSet<StreamObserver<ServerReply>> getGameClients(@NotNull String sessionId) {
        return clientsMapper.getOrDefault(sessionId, new HashSet<>());
    }

    /**
     * Remove client from game session
     * @param sessionId session id
     * @param client client to remove
     */
    public void removeClient(@NotNull String sessionId, StreamObserver<ServerReply> client) {
        HashSet<StreamObserver<ServerReply>> currentClients = clientsMapper.get(sessionId);

        if (currentClients == null) {
            return;
        }

        currentClients.remove(client);
        clientsMapper.put(sessionId, currentClients);
    }

    /**
     * Get game by id
     * @param sessionId game id
     * @return game model if it exists null otherwise
     */
    public GameModel getGameById(@NotNull String sessionId) {
        return games.get(sessionId);
    }

    /**
     * Update game state for specified session
     * @param sessionId session to update
     * @param gameModel new model
     */
    public void setGameById(@NotNull String sessionId, @NotNull GameModel gameModel) {
        games.put(sessionId, gameModel);
    }

    /**
     * Get list of all sessions IDs
     * @return list of sessions
     */
    public Set<String> getAllGames() {
        return  games.keySet();
    }
}
