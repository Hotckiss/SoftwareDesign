package ru.roguelike.logic.generators;

import ru.roguelike.logic.GameModel;

/**
 * Generates a model (start field) for a new game.
 */
public interface GameGenerator {
    /**
     * @return a model of a new game.
     */
    GameModel generate();
}
