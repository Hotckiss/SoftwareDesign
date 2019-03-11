package ru.roguelike;

import ru.roguelike.controller.GameController;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.generators.GameGenerator;
import ru.roguelike.logic.generators.RandomGenerator;
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.ConsoleViewImpl;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        ConsoleView cv = new ConsoleViewImpl();
        GameGenerator generator = new RandomGenerator(15, 15, 0.15, 5, 5);
        GameModel game = generator.generate();

        GameController controller = new GameController(game, cv);
        controller.runGame();
    }
}
