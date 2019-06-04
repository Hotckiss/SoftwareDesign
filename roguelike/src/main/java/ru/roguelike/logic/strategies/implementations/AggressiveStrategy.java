package ru.roguelike.logic.strategies.implementations;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.movable.Player;

import java.io.Serializable;
import java.util.List;

/**
 * Aggressive strategy: attack player in next position
 */
public class AggressiveStrategy extends AbstractStrategy implements Serializable {
    private AbstractStrategy defaultStrategy = new RandomStrategy();

    /**
     * {@inheritDoc}
     */
    @Override
    public Move preferredMove(Position position, GameModel model) {
        List<List<AbstractGameObject>> field = model.getField();
        int x = position.getX();
        int y = position.getY();

        if (x - 1 >= 0 && field.get(x - 1).get(y) instanceof Player) {
            return Move.UP;
        }

        if (x + 1 < field.size() && field.get(x + 1).get(y) instanceof Player) {
            return Move.DOWN;
        }

        if (y - 1 >= 0 && field.get(x).get(y - 1) instanceof Player) {
            return Move.LEFT;
        }

        if (y + 1 < field.get(x).size() && field.get(x).get(y + 1) instanceof Player) {
            return Move.RIGHT;
        }

        return defaultStrategy.preferredMove(position, model);
    }
}
