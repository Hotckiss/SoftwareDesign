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
        private void sendModelToAllPlayers(String sessionName) {
            ServerReply.Builder responseBuilder = ServerReply.newBuilder();
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
                    System.out.println("sssssssssssssssss");
                    // player request to list all sessions list
                    if (request.getSessionName().equals("list")) {
                        Set<String> allGames = manager.getAllGames();
                        StringJoiner joiner = new StringJoiner("\n");
                        for (String id: allGames) {
                            joiner.add(id);
                        }

                        String list = joiner.toString();
                        ServerReply response = ServerReply.newBuilder().setSessions(list).build();
                        responseObserver.onNext(response);
                    } else if(sessionName == null) {
                        ServerReply.Builder builder = ServerReply.newBuilder();
                        String sessionName = request.getSessionName();
                        manager.addClientToGame(sessionName, responseObserver);
                        GameModel model = manager.getGameById(sessionName);
                        //Integer playerId = model.addPlayer();
                        builder.setPlayerId(playerId.toString());
                    } else if (!request.getAction().isEmpty()){
                        GameModel model = manager.getGameById(sessionName);
                        //if (playerId != model.getActivePlayer()) {
                        //    return;
                        //}

                        String errorMessage = null;
                        try {
                            //Command.fromByteArray(request.getAction().toByteArray()).execute();
                        } catch( Exception ex) {
                            errorMessage = "Error occurred";
                            ex.printStackTrace();
                        }

                        if (errorMessage != null) {
                            sendErrosMessage(errorMessage, responseObserver);
                        } else {
                            sendModelToAllPlayers(sessionName);
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
