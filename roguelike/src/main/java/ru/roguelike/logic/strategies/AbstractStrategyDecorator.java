package ru.roguelike.logic.strategies;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.objects.movable.Mob;

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
    public Move preferredMove(Mob mob, GameModel model) {
        return decoratee.preferredMove(mob, model);
    }
}
