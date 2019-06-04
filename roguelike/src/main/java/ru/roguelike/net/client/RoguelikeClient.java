package ru.roguelike.net.client;

import com.googlecode.lanterna.screen.Screen;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.roguelike.PlayerRequest;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.RoguelikeServiceGrpc;
import ru.roguelike.ServerReply;
import ru.roguelike.controller.GameController;
import ru.roguelike.logic.GameModel;
import ru.roguelike.view.UserInputProvider;
import ru.roguelike.view.UserInputProviderImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class RoguelikeClient {
    private ManagedChannel channel;
    private AtomicReference<StreamObserver<PlayerRequest>> communicatorRef = new AtomicReference<>();
    private StreamObserver<PlayerRequest> communicator;
    private RoguelikeServiceGrpc.RoguelikeServiceStub stub;
    private boolean isListLastOperation = false;
    private final GameController controller;
    private AtomicBoolean isGameInitialized = new AtomicBoolean(false);
    private boolean isFinished = false;
    private final Object lock = new Object();
    private GameModel clientModel = null;
    private Integer playerServerId;

    public RoguelikeClient(final String host, final Integer port, final GameController controller) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.channel = channel;
        this.stub = RoguelikeServiceGrpc.newStub(channel);
        this.communicator = stub.communicate(new ServerResponseHandler());
        communicatorRef.set(this.communicator);
        this.controller = controller;
    }

    private void connect(String name) {
        System.out.println("Will try to connect to {$name} session...");
        PlayerRequest request = PlayerRequest.newBuilder().setSessionId(name).build();
        isListLastOperation = false;
        communicator.onNext(request);
    }

    private void list() {
        PlayerRequest request = PlayerRequest.newBuilder().setSessionId("list").build();
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
                    controller.showSessionsList(value.getSessionsList());
                } catch (IOException e) {
                    RoguelikeLogger.INSTANCE.log_error(e.getMessage());
                }

                return;
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(value.getModel().toByteArray());
            ObjectInput in = null;
            try {
                in = new ObjectInputStream(bis);
                Object o = in.readObject();
                if (clientModel == null) {
                    isGameInitialized.set(true);
                    playerServerId = Integer.parseInt(value.getPlayerId());
                }
                clientModel = (GameModel)o;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    // ignore close exception
                }
            }

            controller.setGame(clientModel);

            try {
                controller.updateOnlineGame(playerServerId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable t) {

        }

        @Override
        public void onCompleted() {
            System.out.println("Finish");
            synchronized(lock) {
                isFinished = true;
                lock.notifyAll();
            }
        }
    }

    public void start() throws IOException, InterruptedException {
        boolean sessionIsChosen = false;
        list();

        while (!sessionIsChosen) {
            String inputCommand = controller.getInputForChar().getCharacter().toString();
            System.out.println(inputCommand);

            connect(inputCommand);
            Thread threadToReadInput = new Thread(() -> {
                while (!isGameInitialized.get()) {
                }
                while (!isFinished) {
                    try {
                        controller.makeOnlineTurn(communicatorRef.get());
                    } catch (IOException e) {
                        RoguelikeLogger.INSTANCE.log_error(e.getMessage());
                    }
                }
            });

            threadToReadInput.start();
            sessionIsChosen = true;
        }

        while (!isFinished) {
            synchronized(lock) {
                lock.wait();
            }
        }
    }
}
