package ru.roguelike.logic.strategies;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.objects.movable.Mob;

import java.io.Serializable;

/**
 * Interface for preferred move decisions
 */
public abstract class AbstractStrategy implements Serializable {
    /**
     * Generate preferred movement
     * @param mob current mob
     * @param model game situation
     * @return preferred movement
     */
    public abstract Move preferredMove(Mob mob, GameModel model);
}
