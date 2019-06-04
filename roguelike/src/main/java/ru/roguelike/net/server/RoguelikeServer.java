package ru.roguelike.net.server;

import com.google.protobuf.ByteString;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ru.roguelike.ConnectionSetUpperGrpc;
import ru.roguelike.PlayerRequest;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.ServerReply;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.generators.RandomGenerator;
import ru.roguelike.view.StringStreamInputProviderImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.StringJoiner;

public class RoguelikeServer {
    private int port;
    private Server server = null;
    private SessionsManager manager = new SessionsManager();

    public RoguelikeServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 22228;
        System.out.println("Start creating server");
        RoguelikeServer server = new RoguelikeServer(port);
        System.out.println("Starting server....");
        server.start();
        System.out.println("Server started....");
        server.blockUntilShutdown();
        System.out.println("Shutdown...");
    }

    private void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new RoguelikeService())
                .build()
                .start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                stop();
                System.err.println("*** server shut down");
            }
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private class RoguelikeService extends ConnectionSetUpperGrpc.ConnectionSetUpperImplBase {
        private void sendModelToAllPlayers(String sessionName, ServerReply.Builder responseBuilder) {
            if (responseBuilder == null) {
                responseBuilder = ServerReply.newBuilder();
            }

            GameModel model = manager.getGameById(sessionName);

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
                    // ignore close exception
                }
            }

            responseBuilder.setModel(ByteString.copyFrom(bos.toByteArray()));

            ServerReply response = responseBuilder.build();
            for (StreamObserver<ServerReply> client: manager.getGameClients(sessionName)) {
                client.onNext(response);
            }
        }

        private void sendErrorsMessage(String errorMessage, StreamObserver<ServerReply> client) {
            ServerReply response = ServerReply.newBuilder().setErrorMessage(errorMessage).build();
            client.onNext(response);
        }

        @Override
        public StreamObserver<PlayerRequest> communicate(StreamObserver<ServerReply> responseObserver) {
            return new StreamObserver<PlayerRequest>() {
                private Integer playerId = null;
                private String sessionName = null;

                @Override
                public void onNext(PlayerRequest request) {
                    // player request to list all sessions list
                    if (request.getSessionName().equals("list")) {
                        System.out.println("LIST");
                        Set<String> allGames = manager.getAllGames();
                        StringJoiner joiner = new StringJoiner("\n");
                        for (String id: allGames) {
                            joiner.add(id);
                        }

                        String list = joiner.toString();
                        ServerReply response = ServerReply
                                .newBuilder()
                                .setSessions(list)
                                .build();
                        responseObserver.onNext(response);
                    } else if(sessionName == null) { // if no sessions was associated to this player
                        System.out.println("NEW PLAYER CHOOSE SESSION");
                        ServerReply.Builder builder = ServerReply.newBuilder();
                        // get player input session
                        sessionName = request.getSessionName();
                        // add to game or create
                        manager.addClientToGame(sessionName, responseObserver);

                        // add player to game and return his identifier
                        GameModel model = manager.getGameById(sessionName);
                        if (model == null) {
                            model = new RandomGenerator(15, 15, 0.15, 5, 5).generate();
                            playerId = 0;
                        } else {
                            playerId = model.addPlayerRandom();
                        }

                        manager.setGameById(sessionName, model);
                        builder.setPlayerId(playerId.toString());

                        sendModelToAllPlayers(sessionName, builder);
                    } else if (!request.getAction().isEmpty()){
                        System.out.println(request.getAction());
                        GameModel model = manager.getGameById(sessionName);
                        System.out.println(model.getPlayer().getPosition().getX());
                        System.out.println("----------");
                        System.out.println(playerId);
                        System.out.println(model.getActivePlayerId());
                        System.out.println("----------");
                        if (!playerId.equals(model.getActivePlayerId())) {
                            return;
                        }

                        String errorMessage = null;
                        try {
                            model.makeMove(new StringStreamInputProviderImpl(request.getAction()));
                        } catch (IOException e) {
                            errorMessage = e.getMessage();
                        }
                        manager.setGameById(sessionName, model);
                        System.out.println(model.getPlayer().getPosition().getX());
                        if (errorMessage != null) {
                            sendErrorsMessage(errorMessage, responseObserver);
                        } else {
                            sendModelToAllPlayers(sessionName, null);
                        }
                    }
                }

                @Override
                public void onError(Throwable t) {
                    responseObserver.onError(t);
                }

                @Override
                public void onCompleted() {
                    GameModel model = manager.getGameById(sessionName);
                    model.removePlayer(playerId);
                    manager.setGameById(sessionName, model);
                    manager.removeClient(sessionName, responseObserver);
                    sendModelToAllPlayers(sessionName, null);
                }
            };
        }
    }
}
