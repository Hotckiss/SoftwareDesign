package ru.roguelike.logic.strategies;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;

public class AbstractStrategyDecorator extends AbstractStrategy {
    protected AbstractStrategy decoratee;

    public AbstractStrategyDecorator(AbstractStrategy decoratee) {
        this.decoratee = decoratee;
    }

    @Override
    public Move preferredMove(Position position, GameModel model) {
        return decoratee.preferredMove(position, model);
    }
}
