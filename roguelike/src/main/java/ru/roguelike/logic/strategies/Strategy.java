package ru.roguelike.logic.strategies;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;

/**
 * Interface for preferred move decisions
 */
public interface Strategy {
    /**
     * Generate preferred movement
     * @param position current position
     * @param model game situation
     * @return preferred movement
     */
    Move preferredMove(Position position, GameModel model);
}
