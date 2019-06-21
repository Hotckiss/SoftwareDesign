package ru.roguelike.net.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.roguelike.PlayerRequest;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.RoguelikeServiceGrpc;
import ru.roguelike.ServerReply;
import ru.roguelike.controller.GameController;
import ru.roguelike.logic.GameModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class RoguelikeClient {
    /**
     * Communication with server class
     */
    private final StreamObserver<PlayerRequest> communicator;
    /**
     * If true, list of sessions was queried
     */
    private boolean isListQuery = false;
    /**
     * Game controller
     */
    private final GameController controller;

    /**
     * True if prepared game model from server. AtomicBoolean for usage from inner class
     */
    private final AtomicBoolean didPrepareGame = new AtomicBoolean(false);

    /**
     * Finish client flag
     */
    private boolean isFinished = false;

    /**
     * Lock to wait
     */
    private final Object lock = new Object();
    /**
     * Game state
     */
    private GameModel clientModel = null;
    /**
     * Player id, used to query correct info to display
     */
    private Integer playerServerId;

    /**
     * Constructs new client connected to specified host
     * @param host host to connect
     * @param port port to connect
     * @param controller game controller
     */
    public RoguelikeClient(final String host, final Integer port, final GameController controller) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.communicator = RoguelikeServiceGrpc.newStub(channel).communicate(new ServerResponseHandler());
        this.controller = controller;
    }

    private class ServerResponseHandler implements StreamObserver<ServerReply> {

        @Override
        public void onNext(ServerReply value) {
            if (!value.getErrorMessage().isEmpty()) {
                RoguelikeLogger.INSTANCE.log_error(value.getErrorMessage());
                return;
            }

            if (isListQuery) {
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
                // first received model
                if (clientModel == null) {
                    didPrepareGame.set(true);
                    playerServerId = Integer.parseInt(value.getPlayerId());
                }
                clientModel = (GameModel)o;
            } catch (IOException | ClassNotFoundException e) {
                RoguelikeLogger.INSTANCE.log_error(e.getMessage());
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    RoguelikeLogger.INSTANCE.log_error(e.getMessage());
                }
            }

            controller.setGame(clientModel);

            try {
                controller.updateOnlineGame(playerServerId);
                if (!clientModel.getPlayerById(playerServerId).isAlive()) {
                    communicator.onCompleted();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable t) {
            RoguelikeLogger.INSTANCE.log_error(t.getMessage());
        }

        @Override
        public void onCompleted() {
            RoguelikeLogger.INSTANCE.log_error("Finish game");
            synchronized(lock) {
                isFinished = true;
                lock.notifyAll();
            }
        }
    }

    /**
     * Starts client and queries list of sessions.
     * Than runs into loop of game
     * @throws IOException if any I/O error occurred
     * @throws InterruptedException if game thread was interrupted
     */
    public void start() throws IOException, InterruptedException {
        //query all sessions first
        boolean gameSelected = false;
        isListQuery = true;
        RoguelikeLogger.INSTANCE.log_info("Query list of sessions");
        communicator.onNext(PlayerRequest.newBuilder().build());

        while (!gameSelected) {
            //read input session name and connect
            String inputCommand = controller.getLine();
            RoguelikeLogger.INSTANCE.log_info(inputCommand + " session selected");

            isListQuery = false;
            communicator.onNext(PlayerRequest.newBuilder().setSessionId(inputCommand).build());

            new Thread(() -> {
                while (!didPrepareGame.get()) {
                }
                while (!isFinished) {
                    try {
                        controller.makeOnlineTurn(communicator);
                    } catch (IOException e) {
                        RoguelikeLogger.INSTANCE.log_error(e.getMessage());
                    }
                }
            }).start();

            gameSelected = true;
        }

        while (!isFinished) {
            synchronized(lock) {
                lock.wait();
            }
        }
    }
}
