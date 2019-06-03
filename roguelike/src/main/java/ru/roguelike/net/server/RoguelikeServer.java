package ru.roguelike.net.server;

import io.grpc.stub.StreamObserver;
import ru.roguelike.ConnectionSetUpperGrpc;
import ru.roguelike.PlayerRequest;
import ru.roguelike.ServerReply;

public class RoguelikeServer {
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
