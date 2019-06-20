package ru.roguelike.net.server;

import com.google.protobuf.ByteString;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.PlayerRequest;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.RoguelikeServiceGrpc;
import ru.roguelike.ServerReply;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.commands.ApplyTurnCommand;
import ru.roguelike.logic.generators.RandomGenerator;
import ru.roguelike.view.StringStreamInputProviderImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.StringJoiner;

public class RoguelikeServer {
    private Server server;
    private SessionsManager manager = new SessionsManager();

    /**
     * Construct server with port
     * @param port port number
     */
    public RoguelikeServer(int port) {
        server = ServerBuilder.forPort(port)
                .addService(new RoguelikeService())
                .build();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 22228;
        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                RoguelikeLogger.INSTANCE.log_info("Invalid port. Using default: 22228");
            }
        }

        RoguelikeServer server = new RoguelikeServer(port);
        server.start();
        server.awaitTermination();
    }

    /**
     * Start server
     */
    public void start() throws IOException {
        server.start();
    }

    /**
     * Await termination method
     */
    public void awaitTermination() throws InterruptedException {
        server.awaitTermination();
    }

    /**
     * Service to manage clients
     */
    private class RoguelikeService extends RoguelikeServiceGrpc.RoguelikeServiceImplBase {
        @Override
        public StreamObserver<PlayerRequest> communicate(StreamObserver<ServerReply> responseObserver) {
            return new StreamObserver<PlayerRequest>() {
                private Integer playerId = null;
                private String sessionId = null;
                private boolean isHelloWasSaid = false;

                @Override
                public void onNext(PlayerRequest request) {
                    ServerReply.Builder response = ServerReply.newBuilder();

                    // player request to list all sessions list
                    if (!isHelloWasSaid) {
                        isHelloWasSaid = true;
                        Set<String> allGames = manager.getAllGames();

                        StringJoiner joiner = new StringJoiner("\n");
                        for (String id: allGames) {
                            joiner.add(id);
                        }

                        String list = joiner.toString();
                        if (allGames.isEmpty()) {
                            list = "No sessions. Create new game!";
                        }

                        responseObserver.onNext(response.setSessionsList(list).build());
                    } else if(sessionId == null) { // if no sessions was associated to this player

                        // get player input session
                        sessionId = request.getSessionId();
                        // add to game or create
                        manager.addClientToGame(sessionId, responseObserver);

                        // add player to game and return his identifier
                        GameModel model = manager.getGameById(sessionId);
                        if (model == null) { // create new game
                            model = new RandomGenerator(15, 15, 0.15, 5, 5).generate();
                            playerId = 0;
                        } else {
                            playerId = model.addPlayerRandom();
                        }

                        manager.setGameById(sessionId, model);
                        response.setPlayerId(playerId.toString());

                        updateClientsWithModel(sessionId, response);
                    } else if (!request.getAction().isEmpty()){
                        GameModel model = manager.getGameById(sessionId);
                        if (!playerId.equals(model.getActivePlayerId())) { // not players turn
                            return;
                        }

                        try {
                            model.makeMove(ApplyTurnCommand.applyPlayerAction(new StringStreamInputProviderImpl(request.getAction()), model));
                        } catch (IOException e) {
                            responseObserver.onNext(ServerReply.newBuilder().setErrorMessage(e.getMessage()).build());
                            return;
                        }

                        manager.setGameById(sessionId, model);
                        updateClientsWithModel(sessionId, response);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    responseObserver.onError(t);
                }

                @Override
                public void onCompleted() {
                    GameModel model = manager.getGameById(sessionId);
                    model.removePlayer(playerId);
                    manager.setGameById(sessionId, model);
                    manager.removeClient(sessionId, responseObserver);
                    updateClientsWithModel(sessionId, ServerReply.newBuilder());
                }
            };
        }

        private void updateClientsWithModel(@NotNull String sessionId,
                                            @NotNull ServerReply.Builder response) {
            GameModel model = manager.getGameById(sessionId);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out;
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(model);
                out.flush();
            } catch (IOException e) {
                RoguelikeLogger.INSTANCE.log_error(e.getMessage());
            } finally {
                try {
                    bos.close();
                } catch (IOException ex) {
                    RoguelikeLogger.INSTANCE.log_info(ex.getMessage());
                }
            }

            response.setModel(ByteString.copyFrom(bos.toByteArray()));

            ServerReply responseReady = response.build();
            for (StreamObserver<ServerReply> client: manager.getGameClients(sessionId)) {
                client.onNext(responseReady);
            }
        }
    }
}
