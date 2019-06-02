package ru.roguelike.logic.strategies.implementations;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.models.Position;

/**
 * Passive strategy: not moving, todo: attack player in next position
 */
public class PassiveStrategy extends AbstractStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public Move preferredMove(Position position, GameModel model) {
        return Move.NONE;
    }
}
