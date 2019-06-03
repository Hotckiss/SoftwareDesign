package ru.roguelike.net.client;

import com.googlecode.lanterna.screen.Screen;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.roguelike.ConnectionSetUpperGrpc;
import ru.roguelike.PlayerRequest;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.ServerReply;
import ru.roguelike.controller.GameController;
import ru.roguelike.view.UserInputProvider;
import ru.roguelike.view.UserInputProviderImpl;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class RoguelikeClient {
    private ManagedChannel channel;
    private AtomicReference<StreamObserver<PlayerRequest>> communicatorRef = new AtomicReference<>();
    private StreamObserver<PlayerRequest> communicator;
    private ConnectionSetUpperGrpc.ConnectionSetUpperStub stub;
    private boolean isListLastOperation = false;
    private final GameController controller;

    public RoguelikeClient(final String host, final Integer port, final GameController controller) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.channel = channel;
        this.stub = ConnectionSetUpperGrpc.newStub(channel);
        this.communicator = stub.communicate(new ServerResponseHandler());
        communicatorRef.set(this.communicator);
        this.controller = controller;
    }

    private void connect(String name) {
        System.out.println("Will try to connect to {$name} session...");
        PlayerRequest request = PlayerRequest.newBuilder().setSessionName(name).build();
        isListLastOperation = false;
        communicator.onNext(request);
    }

    private void list() {
        PlayerRequest request = PlayerRequest.newBuilder().setSessionName("list").build();
        isListLastOperation = true;
        communicator.onNext(request);
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private class ServerResponseHandler implements StreamObserver<ServerReply> {

        @Override
        public void onNext(ServerReply value) {
            if (!value.getErrorMessage().isEmpty()) {
                System.out.println("Error on server after action");
            }

            if (isListLastOperation) {
                try {
                    controller.showSessionsList(value.getSessions());
                } catch (IOException e) {
                    RoguelikeLogger.INSTANCE.log_error(e.getMessage());
                }
            }
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onCompleted() {
            System.out.println("Finish");
        }
    }

    public void start() throws IOException {
        boolean sessionIsChosen = false;
        list();

        while (!sessionIsChosen) {
            String inputCommand = controller.getInputForChar().getCharacter().toString();
            System.out.println(inputCommand);

            connect(inputCommand);
            Thread threadToReadInput = new Thread(new Runnable() {
                @Override
                public void run() {
                    //while (!isGameInitialized.get()) {
                    //    continue;
                    //}
                    //while (!isFinished) {
                    //    Controller.makeOnlineTurn(view!!, communicatorRef.get());
                    //}
                }
            });

            threadToReadInput.start();
            sessionIsChosen = true;
        }
        System.out.println("Finished");
        //while (!isFinished) {
        //    ;
        //}
    }
}
