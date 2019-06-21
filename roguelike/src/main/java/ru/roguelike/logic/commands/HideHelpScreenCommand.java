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

    /**
     * Constructs new command to hide help screen
     * @param model current game state
     * @param view view to output game model
     */
    public HideHelpScreenCommand(GameModel model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws IOException {
        view.clear();
        view.draw(model.getField(), model.getInfo(), model.getLog(model.getActivePlayerId()));
    }
}
