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

    public AbstractStrategyDecorator(AbstractStrategy decoratee) {
        this.decoratee = decoratee;
    }

    @Override
    public Move preferredMove(Position position, GameModel model) {
        return decoratee.preferredMove(position, model);
    }
}
