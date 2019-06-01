package ru.roguelike.logic.strategies.implementations;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.Strategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Coward strategy: not attack player in next position
 */
public class CowardStrategy implements Strategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public Move preferredMove(Position position, GameModel model) {
        List<Move> availableMoves = new ArrayList<>();
        availableMoves.add(Move.NONE);

        List<List<AbstractGameObject>> field = model.getField();
        int x = position.getX();
        int y = position.getY();

        if (x - 1 >= 0 && StrategiesUtils.availableForCowardMob(field, x - 1, y)) {
            availableMoves.add(Move.TOP);
        }

        if (x + 1 < field.size() && StrategiesUtils.availableForCowardMob(field, x + 1, y)) {
            availableMoves.add(Move.DOWN);
        }

        if (y - 1 >= 0 && StrategiesUtils.availableForCowardMob(field, x, y - 1)) {
            availableMoves.add(Move.LEFT);
        }

        if (y + 1 < field.get(x).size() && StrategiesUtils.availableForCowardMob(field, x, y + 1)) {
            availableMoves.add(Move.RIGHT);
        }

        Random random = new Random();

        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
}
