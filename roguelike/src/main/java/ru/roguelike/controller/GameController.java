package ru.roguelike.controller;

import com.googlecode.lanterna.screen.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.MenuOption;
import ru.roguelike.logic.commands.*;
import ru.roguelike.logic.gameloading.GameSaverAndLoader;
import ru.roguelike.logic.generators.GameGenerator;
import ru.roguelike.logic.generators.RandomGenerator;
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.UserInputProvider;
import ru.roguelike.view.UserInputProviderImpl;

import java.io.IOException;

/**
 * Controls the game process.
 */
public class GameController {
    private GameModel game = null;
    private ConsoleView view;

    /**
     * Creates new game controller with specified model
     * @param view game view
     */
    public GameController(@NotNull ConsoleView view) {
        this.view = view;
    }

    public void setGame(GameModel game) {
        this.game = game;
    }

    public GameModel getGame() {
        return game;
    }

    public void selectMode() throws IOException {
        while (true) {
            MenuOption selection = view.showMenu();
            GameModel newGame = null;
            try {
                newGame = startGameFromSelection(selection);
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

    private GameModel startGameFromSelection(MenuOption selection) throws Exception {
        switch (selection) {
            case NEW_GAME:
                GameGenerator generator = new RandomGenerator(15, 15, 0.15, 5, 5);
                return generator.generate();
            case LOAD_GAME:
                GameSaverAndLoader saverAndLoader = new GameSaverAndLoader();
                GameModel newGame = saverAndLoader.loadGame();
                if (newGame == null) {
                    throw new Exception("There is not any saved games!");
                }
                return newGame;
            case ONLINE_GAME:
                break;
        }

        return null;
    }

    private void runGame() throws IOException {
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
            Screen screen = GameController.this.view.getScreen();
            screen.refresh();
            UserInputProvider provider = new UserInputProviderImpl(screen.readInput());
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
