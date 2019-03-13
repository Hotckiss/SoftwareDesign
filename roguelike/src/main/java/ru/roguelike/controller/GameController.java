package ru.roguelike.controller;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.logic.GameModel;
import ru.roguelike.view.ConsoleView;

import java.io.IOException;

/**
 * Controls the game process.
 */
public class GameController {
    private GameModel game;
    private ConsoleView view;

    public GameController(@NotNull GameModel model, @NotNull ConsoleView view) {
        this.game = model;
        this.view = view;
    }

    /**
     * Starts the game process
     *
     * @throws IOException if it occurs during user's input reading.
     */
    public void runGame() throws IOException {
        RoguelikeLogger.INSTANCE.log_info("Game started");
        while (!game.finished()) {
            view.clear();
            view.draw(game.makeDrawable(), game.getInfo(), game.getLog(), game.isShowHelpScreen());
            game.makeAction(view.getScreen());
        }
        RoguelikeLogger.INSTANCE.log_info("Game finished");
        view.clear();
        view.draw(game.makeDrawable(), game.getInfo(), game.getLog(), game.isShowHelpScreen());
    }
}
