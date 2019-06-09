package ru.roguelike.controller;

import io.grpc.stub.StreamObserver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.roguelike.PlayerRequest;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.MenuOption;
import ru.roguelike.logic.commands.*;
import ru.roguelike.logic.gameloading.GameSaverAndLoader;
import ru.roguelike.logic.generators.GameGenerator;
import ru.roguelike.logic.generators.RandomGenerator;
import ru.roguelike.net.client.RoguelikeClient;
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;

/**
 * Controls the game process.
 */
public class GameController {
    private static final String AVAILABLE_ONLINE_ACTIONS = "wasdqe";
    private GameModel game = null;
    private ConsoleView view;

    /**
     * Creates new game controller with specified model
     * @param view game view
     */
    public GameController(@NotNull ConsoleView view) {
        this.view = view;
    }

    /**
     * Setter for game model
     * @param game new game
     */
    public void setGame(GameModel game) {
        this.game = game;
    }

    /**
     * Getter for game model
     * @return current game
     */
    public GameModel getGame() {
        return game;
    }

    /**
     * Method to read line from console view
     * @return resulting line
     * @throws IOException if any I/O error occurred
     */
    public String getLine() throws IOException {
        return view.readLineFromScreen();
    }

    /**
     * Method to output available sessions from server
     * @param sessionsList available sessions
     * @throws IOException if any I/O error occurred
     */
    public void showSessionsList(String sessionsList) throws IOException {
        view.showSessionsList(sessionsList);
    }

    /**
     * Method to select game mode
     * @throws IOException if any I/O error occurred
     */
    public void selectMode() throws IOException {
        while (true) {
            MenuOption selection = view.showMenu();
            GameModel newGame = null;
            try {
                switch (selection) {
                    case NEW_GAME:
                        GameGenerator generator = new RandomGenerator(15, 15, 0.15, 5, 5);
                        newGame = generator.generate();
                        break;
                    case LOAD_GAME:
                        GameSaverAndLoader saverAndLoader = new GameSaverAndLoader();
                        newGame = saverAndLoader.loadGame();
                        break;
                    case ONLINE_GAME:
                        processOnlineGame();
                        break;
                }
            } catch (Exception e) {
                RoguelikeLogger.INSTANCE.log_error(e.getMessage());
            }

            if (newGame != null) {
                game = newGame;
                break;
            }
        }

        runGame();
    }

    /**
     * Method to process turn online. Error turns will be ignored by server
     * @param observer client requests stream
     * @throws IOException if any I/O error occurred
     */
    public void makeOnlineTurn(StreamObserver<PlayerRequest> observer) throws IOException {
        view.refreshScreen();
        UserInputProvider provider = view.makeInputProvider();

        if (provider.isEscape()) {
            observer.onCompleted();
            System.exit(0);
            return;
        }

        RoguelikeLogger.INSTANCE.log_info("Input from user: " + provider.getCharacter());
        if (provider.getCharacter() == null) {
            return;
        }

        String turn = provider.getCharacter().toString();

        if (AVAILABLE_ONLINE_ACTIONS.contains(turn)) {
            observer.onNext(PlayerRequest.newBuilder().setAction(turn).build());
        }
    }

    /**
     * Method to force redraw game model that was received from server
     * @param playerId id of player to display info
     * @throws IOException if any I/O error occurred
     */
    public void updateOnlineGame(Integer playerId) throws IOException {
        //force redraw after selection
        view.clear();
        view.draw(game.getField(), game.getInfo(), game.getLog(playerId));
    }

    private void processOnlineGame() throws IOException, InterruptedException {
        String serverInfo = view.showOnlineMenu();
        String[] parts = serverInfo.split(" ");
        String host = parts[0];
        Integer port = Integer.parseInt(parts[1]);

        RoguelikeClient client = new RoguelikeClient(host, port, this);
        client.start();
    }

    private void runGame() throws IOException {
        //force redraw after selection
        view.clear();
        view.draw(game.getField(), game.getInfo(), game.getLog(game.getActivePlayerId()));

        RoguelikeLogger.INSTANCE.log_info("Game started");
        Invoker invoker = new Invoker();
        while (!game.finished()) {
            invoker.makeAction();
        }
        RoguelikeLogger.INSTANCE.log_info("Game finished");
    }

    /**
     * Takes input from user and invokes corresponding command.
     */
    private class Invoker {
        /**
         * Reads input and makes a corresponding action.
         *
         * @throws IOException if it occurs.
         */
        void makeAction() throws IOException {
            view.refreshScreen();
            UserInputProvider provider = view.makeInputProvider();
            RoguelikeLogger.INSTANCE.log_info("Input from user: " + provider.getCharacter());
            Command command = createCommand(provider);
            if (command != null) {
                command.execute();
            }
        }

        /**
         * @param provider is input from user
         * @return an instance of command corresponding to user's input
         */
        @Nullable
        private Command createCommand(@NotNull UserInputProvider provider) {
            if (provider.getCharacter() == null) {
                return null;
            }
            switch (provider.getCharacter()) {
                case 'l':
                    return new LoadMapCommand(GameController.this.view,
                            GameController.this);
                case 'h':
                    return new ShowHelpScreenCommand(GameController.this.view);
                case 'r':
                    return new HideHelpScreenCommand(GameController.this.game,
                            GameController.this.view);
                case 'v':
                    return new SaveGameCommand(GameController.this.game);
                default:
                    return new ApplyMoveCommand(provider,
                            GameController.this.game,
                            GameController.this.view);
            }
        }
    }
}
