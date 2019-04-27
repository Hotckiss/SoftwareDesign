package ru.roguelike.logic.commands;

import ru.roguelike.controller.GameController;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.generators.FromFileGenerator;
import ru.roguelike.view.ConsoleView;

import java.io.IOException;

/**
 * Loads map from file.
 */
public class LoadMapCommand implements Command {
    private final GameController controller;
    private ConsoleView view;

    public LoadMapCommand(ConsoleView view, GameController controller) {
        this.view = view;
        this.controller = controller;
    }

    @Override
    public void execute() throws IOException {
        String filename = view.getMapFileName();
        FromFileGenerator generator = new FromFileGenerator(filename);
        GameModel newModel = generator.generate();
        if (newModel == null) {
            controller.getGame().setErrorWhileLoadingMap(true);
        } else {
            controller.setGame(newModel);
        }
        GameModel model = controller.getGame();
        view.clear();
        view.draw(model.makeDrawable(), model.getInfo(), model.getLog());
    }
}
