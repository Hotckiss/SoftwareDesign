package ru.roguelike.logic.strategies;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.implementations.RandomStrategy;
import ru.roguelike.models.Position;

/**
 * Confusing decoration of strategy
 */
public class ConfusedStrategyDecorator extends AbstractStrategyDecorator {
    private int confusedCount;

    public ConfusedStrategyDecorator(AbstractStrategy decoratee, int confusedCount) {
        super(decoratee);
        this.confusedCount = confusedCount;
    }

    @Override
    public Move preferredMove(Position position, GameModel model) {
        if (confusedCount > 0) {
            confusedCount--;
            return new RandomStrategy().preferredMove(position, model);
        }

        return decoratee.preferredMove(position, model);
    }
}
