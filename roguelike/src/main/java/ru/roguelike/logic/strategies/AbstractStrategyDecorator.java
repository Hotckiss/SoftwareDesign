package ru.roguelike.logic.strategies;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;

import java.io.Serializable;

/**
 * Decorator for strategies
 */
public class AbstractStrategyDecorator extends AbstractStrategy implements Serializable {
    protected AbstractStrategy decoratee;

    /**
     * Constructs new decorator with strategy to decoratee
     * @param decoratee strategy to decoratee
     */
    public AbstractStrategyDecorator(AbstractStrategy decoratee) {
        this.decoratee = decoratee;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Move preferredMove(Position position, GameModel model) {
        return decoratee.preferredMove(position, model);
    }
}
