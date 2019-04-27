package ru.roguelike.logic.commands;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.gameloading.GameSaverAndLoader;

import java.io.IOException;

public class SaveGameCommand implements Command {
    private final GameModel model;

    public SaveGameCommand(GameModel model) {
        this.model = model;
    }
    @Override
    public void execute() throws IOException {
        model.setSavedGameEqualToCurrent(true);
        GameSaverAndLoader saverAndLoader = new GameSaverAndLoader();
        saverAndLoader.saveGame(model);
    }
}
