package ru.roguelike.logic.commands;

import ru.roguelike.logic.GameModel;
import ru.roguelike.view.ConsoleView;

import java.io.IOException;

/**
 * Hides help screen.
 */
public class HideHelpScreenCommand implements Command {
    private final GameModel model;
    private final ConsoleView view;

    public HideHelpScreenCommand(GameModel model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void execute() throws IOException {
        view.clear();
        view.draw(model.makeDrawable(), model.getInfo(), model.getLog());
    }
}
