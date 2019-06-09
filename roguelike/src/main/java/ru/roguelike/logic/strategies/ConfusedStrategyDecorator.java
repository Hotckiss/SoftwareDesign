package ru.roguelike.logic.strategies;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.implementations.RandomStrategy;
import ru.roguelike.models.Position;

import java.io.Serializable;

/**
 * Confusing decoration of strategy
 */
public class ConfusedStrategyDecorator extends AbstractStrategyDecorator implements Serializable {
    private int confusedCount;

    /**
     * Construct new decorator for confising strategy
     * @param decoratee strategy to decorate
     * @param confusedCount count of confusing turns
     */
    public ConfusedStrategyDecorator(AbstractStrategy decoratee, int confusedCount) {
        super(decoratee);
        this.confusedCount = confusedCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Move preferredMove(Position position, GameModel model) {
        if (confusedCount > 0) {
            confusedCount--;
            return new RandomStrategy().preferredMove(position, model);
        }

        return decoratee.preferredMove(position, model);
    }
}
