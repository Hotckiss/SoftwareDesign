package ru.roguelike.logic.commands;

import ru.roguelike.controller.GameController;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.generators.FromFileGenerator;
import ru.roguelike.view.ConsoleView;

import java.io.IOException;
import java.util.List;

/**
 * Loads map from file.
 */
public class LoadMapCommand implements Command {
    private final GameController controller;
    private ConsoleView view;

    /**
     * Constructs new command to load map
     * @param view view to output game model
     * @param controller current game controller
     */
    public LoadMapCommand(ConsoleView view, GameController controller) {
        this.view = view;
        this.controller = controller;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws IOException {
        String filename = view.getMapFileName();
        FromFileGenerator generator = new FromFileGenerator(filename);
        GameModel newModel = generator.generate();
        String errorLoadingMapMessage = null;
        if (newModel == null) {
            errorLoadingMapMessage = "Error while loading map!";
        } else {
            controller.setGame(newModel);
        }
        GameModel model = controller.getGame();
        view.clear();
        List<String> gameLog = model.getLog(model.getActivePlayerId());

        if (errorLoadingMapMessage != null) {
            gameLog.add(errorLoadingMapMessage);
        }

        view.draw(model.getField(), model.getInfo(), gameLog);
    }
}
