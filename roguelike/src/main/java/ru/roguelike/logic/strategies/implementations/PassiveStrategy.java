package ru.roguelike.logic.strategies.implementations;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.models.Position;

import java.io.Serializable;

/**
 * Passive strategy: not moving, todo: attack player in next position
 */
public class PassiveStrategy extends AbstractStrategy implements Serializable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Move preferredMove(Position position, GameModel model) {
        return Move.NONE;
    }
}
