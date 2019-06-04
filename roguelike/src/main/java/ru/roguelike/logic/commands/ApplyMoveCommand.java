package ru.roguelike.logic.commands;

import ru.roguelike.logic.GameModel;
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;

/**
 * Applies player's move and redraws a field.
 */
public class ApplyMoveCommand implements Command {
    private final UserInputProvider provider;
    private final GameModel model;
    private final ConsoleView view;

    public ApplyMoveCommand(UserInputProvider provider, GameModel
            model, ConsoleView view) {
        this.provider = provider;
        this.model = model;
        this.view = view;
    }

    @Override
    public void execute() throws IOException {
        model.makeMove(provider);
        view.clear();
        view.draw(model.getField(), model.getInfo(), model.getLog(model.getActivePlayerId()));
    }
}
