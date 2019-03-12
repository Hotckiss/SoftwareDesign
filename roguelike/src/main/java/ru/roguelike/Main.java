package ru.roguelike;

import ru.roguelike.controller.GameController;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.generators.GameGenerator;
import ru.roguelike.logic.generators.RandomGenerator;
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.ConsoleViewImpl;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger("Main");
    public static void main(String[] args) throws InterruptedException, IOException {
        logger.info("Launching game");
        ConsoleView cv = new ConsoleViewImpl();
        GameGenerator generator = new RandomGenerator(15, 15, 0.15, 3, 5);
        GameModel game = generator.generate();
        logger.info("Game generated");
        GameController controller = new GameController(game, cv);
        controller.runGame();
    }
}
