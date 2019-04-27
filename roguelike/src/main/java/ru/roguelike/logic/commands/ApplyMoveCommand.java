package ru.roguelike.logic.commands;

import com.googlecode.lanterna.input.KeyStroke;
import ru.roguelike.logic.GameModel;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.ConsoleView;

import java.io.IOException;

/**
 * Applies player's move and redraws a field.
 */
public class ApplyMoveCommand implements Command {
    private final KeyStroke keyStroke;
    private final GameModel model;
    private final ConsoleView view;

    public ApplyMoveCommand(KeyStroke keyStroke, GameModel
            model, ConsoleView view) {
        this.keyStroke = keyStroke;
        this.model = model;
        this.view = view;
    }

    @Override
    public void execute() throws IOException {
        model.makeMove(keyStroke);
        view.clear();
        view.draw(model.makeDrawable(), model.getInfo(), model.getLog());
    }
}
