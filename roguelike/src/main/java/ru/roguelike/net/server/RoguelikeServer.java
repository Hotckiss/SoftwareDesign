package ru.roguelike.net.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.ConnectionSetUpperGrpc;
import ru.roguelike.PlayerRequest;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.ServerReply;

import java.io.IOException;

public class RoguelikeServer {
    private int port;
    private Server server = null;

    public RoguelikeServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 22228;
        RoguelikeServer server = new RoguelikeServer(port);
        server.start();
        server.blockUntilShutdown();
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

    private static class RoguelikeService extends ConnectionSetUpperGrpc.ConnectionSetUpperImplBase {

        @Override
        public StreamObserver<PlayerRequest> communicate(StreamObserver<ServerReply> responseObserver) {
            return new StreamObserver<PlayerRequest>() {
                @Override
                public void onNext(PlayerRequest request) {
                    // player request to list all sessions list
                    if (request.getSessionName().equals("list")) {

                    }

                    //responseObserver.onNext(note);

                }

                @Override
                public void onError(Throwable t) {
                    //logger.log(Level.WARNING, "Encountered error in routeChat", t);
                }

                @Override
                public void onCompleted() {
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
