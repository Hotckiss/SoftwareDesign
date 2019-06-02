package ru.roguelike.controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.commands.*;
import ru.roguelike.view.ConsoleView;

import java.io.IOException;

/**
 * Controls the game process.
 */
public class GameController {
    private GameModel game;
    private ConsoleView view;

    /**
     * Creates new game controller with specified model
     * @param model game model
     * @param view game view
     */
    public GameController(@NotNull GameModel model, @NotNull ConsoleView view) {
        this.game = model;
        this.view = view;
    }

    public void setGame(GameModel game) {
        this.game = game;
    }

    public GameModel getGame() {
        return game;
    }

    /**
     * Starts the game process
     *
     * @throws IOException if it occurs during user's input reading.
     */
    public void runGame() throws IOException {
        RoguelikeLogger.INSTANCE.log_info("Game started");

        String[] menuOptions = game.getStartMenuOptions();
        String error = null;

        while (true) {
            String selection = view.showMenu(menuOptions, error);
            error = null;
            GameModel newGame = null;
            try {
                newGame = game.startGameFromSelection(selection, error);
            } catch (Exception e) {
                error = e.getMessage();
            }

            if (newGame != null) {
                game = newGame;
                break;
            }
        }
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
            KeyStroke keyStroke = screen.readInput();
            RoguelikeLogger.INSTANCE.log_info("Input from user: " + keyStroke.getCharacter());
            Command command = createCommand(keyStroke);
            if (command != null) {
                command.execute();
            }
        }

        /**
         * @param keyStroke is input fromm user
         * @return an instance of command corresponding to user's input
         */
        @Nullable
        private Command createCommand(@NotNull KeyStroke keyStroke) {
            if (keyStroke.getCharacter() == null) {
                return null;
            }
            switch (keyStroke.getCharacter()) {
                case 'l':
                    return new LoadMapCommand(GameController.this.view,
                            GameController.this);
                case 'h':
                    return new ShowHelpScreenCommand(GameController.this.view);
                case 'r':
                    return new HideHelpScreenCommand(GameController.this
                            .game, GameController.this.view);
                case 'v':
                    return new SaveGameCommand(GameController.this.game);
                default:
                    return new ApplyMoveCommand(keyStroke,
                            GameController.this.game, GameController.this.view);
            }
        }
    }
}
