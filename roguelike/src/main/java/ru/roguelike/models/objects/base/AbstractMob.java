package ru.roguelike.models.objects.base;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;

/**
 * Class that represents mob with AI strategy
 */
public abstract class AbstractMob extends AbstractGameParticipant {
    // current mob strategy
    protected AbstractStrategy mobStrategy;
    // default mob strategy
    protected AbstractStrategy defaultStrategy;

    @Override
    public Move move(UserInputProvider provider, GameModel model) throws IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        return mobStrategy.preferredMove(position, model);
    }
}
