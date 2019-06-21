package ru.roguelike.logic.commands;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;
import java.util.List;

/**
 * Applies player's move and redraws a field.
 */
public class ApplyTurnCommand implements Command {
    private final UserInputProvider provider;
    private final GameModel model;
    private final ConsoleView view;

    /**
     * Constructs new command to apply move
     * @param provider provider to read user move
     * @param model current game state
     * @param view view to output move
     */
    public ApplyTurnCommand(UserInputProvider provider, GameModel
            model, ConsoleView view) {
        this.provider = provider;
        this.model = model;
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws IOException {
        model.makeMove(ApplyTurnCommand.applyPlayerAction(provider, model));
        view.clear();
        view.draw(model.getField(), model.getInfo(), model.getLog(model.getActivePlayerId()));
    }

    public static Move applyPlayerAction(UserInputProvider provider, GameModel model) {
        Player currentPlayer = model.getActivePlayer();
        Position playerPosition = currentPlayer.getPosition();

        List<List<AbstractGameObject>> field = model.getField();

        int x = playerPosition.getX();
        int y = playerPosition.getY();

        if (currentPlayer.getFreezeCount() == 0) {
            switch (provider.getCharacter()) {
                case 'w':
                    return (x > 0 && !(field.get(x - 1).get(y) instanceof Wall)) ? Move.UP : Move.NONE;
                case 'a':
                    return (y > 0 && !(field.get(x).get(y - 1) instanceof Wall)) ? Move.LEFT : Move.NONE;
                case 's':
                    return (x + 1 < field.size() && !(field.get(x + 1).get(y) instanceof Wall)) ? Move.DOWN : Move.NONE;
                case 'd':
                    return (y + 1 < field.get(0).size() && !(field.get(x).get(y + 1) instanceof Wall)) ? Move.RIGHT : Move.NONE;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                    int index = provider.getCharacter() - '0' - 1;
                    if (currentPlayer.getArtifacts().size() < index + 1) {
                        break;
                    }

                    if (!currentPlayer.getArtifacts().get(index).equipped()) {
                        currentPlayer.getArtifacts().get(index).equip();
                        currentPlayer.enableArtifact(currentPlayer.getArtifacts().get(index).getItem());
                    } else {
                        currentPlayer.getArtifacts().get(index).disable();
                        currentPlayer.disableArtifact(currentPlayer.getArtifacts().get(index).getItem());
                    }
                    break;
                case '6':
                case '7':
                case '8':
                case '9':
                case '0':
                    int idx = provider.getCharacter() - '0' - 6;
                    if (idx == -6) {
                        idx = 4;
                    }
                    currentPlayer.removeArtifact(idx);
                    break;
            }
        }

        return Move.NONE;
    }
}
