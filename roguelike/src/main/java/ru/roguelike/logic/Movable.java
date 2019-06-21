package ru.roguelike.logic;

import ru.roguelike.view.UserInputProvider;

import java.io.IOException;

/**
 * Represents a movable object.
 */
public interface Movable {
    /**
     * @param model current game model
     * @return corresponding move
     * @throws IOException if it occurs
     */
    Move move(GameModel model) throws IOException;
}
