package ru.roguelike.controller;

import ru.roguelike.logic.GameModel;
import ru.roguelike.view.ConsoleView;

import java.io.IOException;
import java.util.logging.Logger;

public class GameController {
    private static Logger logger = Logger.getLogger("GameController");
    private GameModel game;
    private ConsoleView view;

    public GameController(GameModel model, ConsoleView view) {
        this.game = model;
        this.view = view;
    }

    public void runGame() throws IOException {
        logger.info("Game started");
        while (!game.finished()) {
            view.clear();
            view.draw(game.makeDrawable(), game.getInfo(), game.getLog(), game.isShowHelpScreen());
            game.makeAction(view.getScreen());
        }
        logger.info("Game finished");
        view.clear();
        view.draw(game.makeDrawable(), game.getInfo(), game.getLog(), game.isShowHelpScreen());
    }
}
