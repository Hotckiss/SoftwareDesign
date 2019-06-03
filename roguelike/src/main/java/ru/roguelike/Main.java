package ru.roguelike;

import ru.roguelike.controller.GameController;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.generators.GameGenerator;
import ru.roguelike.logic.generators.RandomGenerator;
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.ConsoleViewImpl;

import java.io.IOException;

/**
 * Launches the application.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        RoguelikeLogger.INSTANCE.log_info("Launching game");
        ConsoleView cv = new ConsoleViewImpl();
        RoguelikeLogger.INSTANCE.log_info("Game generated");
        GameController controller = new GameController(cv);
        controller.selectMode();
    }
}
