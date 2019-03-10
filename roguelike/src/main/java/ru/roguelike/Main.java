package ru.roguelike;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.GameModelImpl;
import ru.roguelike.logic.generators.GameGenerator;
import ru.roguelike.logic.generators.RandomGenerator;
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.ConsoleViewImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        ConsoleView cv = new ConsoleViewImpl();
        GameGenerator generator = new RandomGenerator(15, 15, 0.15, 3, 1);
        GameModel game = generator.generate();

        while (true) {
            cv.clear();
            cv.draw(game.makeDrawable(), game.getInfo(), game.getLog());
            game.makeMove(cv.getScreen());
        }
    }
}
