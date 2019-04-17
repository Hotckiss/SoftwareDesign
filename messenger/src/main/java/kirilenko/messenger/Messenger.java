package kirilenko.messenger;


import java.sql.Timestamp;

import io.grpc.*;
import io.grpc.stub.StreamObserver;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Messenger {
    public TextArea out;
    private String author;
    private MessengerGrpc.MessengerBlockingStub stub;
    private Server server;

    public void setOut(TextArea out) {
        this.out = out;
    }

    public Messenger(String author, int port, String peerAddress, int peerPort) throws IOException {
        this.author = author;
        server = ServerBuilder.forPort(port).addService(new MessengerService()).build().start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> server.shutdown()));
        ManagedChannel channel = ManagedChannelBuilder.forAddress(peerAddress, peerPort).usePlaintext().build();
        stub = MessengerGrpc.newBlockingStub(channel);
    }

    public Boolean sendMessage(String content) {
        Date date = new Date();
        Message message = Message.newBuilder().setAuthor(author).setContent(content)
                .setDate((new Timestamp(date.getTime())).toString()).build();
        try {
            stub.sendMessage(message);
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    public void shutDown() {
        try {
            server.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
        }
    }

    private class MessengerService extends MessengerGrpc.MessengerImplBase {
        @Override
        public void sendMessage(Message request, StreamObserver<Message> responseObserver) {
            out.appendText(String.format("Date: %s \t Author: %s \n %s \n",
                    request.getDate(), request.getAuthor(), request.getContent()));
            responseObserver.onNext(Message.newBuilder().build());
            responseObserver.onCompleted();
        }
    }
}
