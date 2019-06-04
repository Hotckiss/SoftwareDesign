package ru.roguelike.net.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import ru.roguelike.ConnectionSetUpperGrpc;
import ru.roguelike.PlayerRequest;
import ru.roguelike.ServerReply;
import ru.roguelike.logic.GameModel;

import java.io.IOException;
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
            //responseBuilder.setModel(ByteString.copyFrom(model.toByteArray()));

            ServerReply response = responseBuilder.build();
            for (StreamObserver<ServerReply> client: manager.getGameClients(sessionName)) {
                client.onNext(response);
            }
        }

        private void sendErrosMessage(String errorMessage, StreamObserver<ServerReply> client) {
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
                        Integer playerId = model.addPlayerRandom();
                        builder.setPlayerId(playerId.toString());

                        sendModelToAllPlayers(sessionName, builder);
                    } else if (!request.getAction().isEmpty()){
                        
                        GameModel model = manager.getGameById(sessionName);
                        if (!playerId.equals(model.getActivePlayerId())) {
                            return;
                        }

                        String errorMessage = null;
                        /*try {
                            Action.fromByteArray(value.action.toByteArray()).execute(model)
                        } catch (e: FailedLoadException) {
                            errorMessage = "Failed loading map of saved game. Probably you have no saved games.\n" +
                                    "Exception message: " + e.message
                        } catch (e: Exception) {
                            errorMessage = "Unexpected exception: " + e.message
                        }*/
                        if (errorMessage != null) {
                            sendErrosMessage(errorMessage, responseObserver);
                        } else {
                            sendModelToAllPlayers(sessionName, null);
                        }
                    }

                    //responseObserver.onNext(note);

                }

                @Override
                public void onError(Throwable t) {
                    responseObserver.onError(t);
                }

                @Override
                public void onCompleted() {
                    //manager.getGameById(sessionName).removePlayer(playerId);
                    manager.removeClient(sessionName, responseObserver);
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
