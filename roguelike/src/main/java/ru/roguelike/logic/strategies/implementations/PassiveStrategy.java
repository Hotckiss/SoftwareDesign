package ru.roguelike.logic.strategies.implementations;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.models.objects.movable.Mob;

import java.io.Serializable;

/**
 * Passive strategy: not moving
 */
public class PassiveStrategy extends AbstractStrategy implements Serializable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Move preferredMove(Mob mob, GameModel model) {
        return Move.NONE;
    }
}
