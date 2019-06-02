package ru.roguelike.logic;

import ru.roguelike.view.UserInputProvider;

import java.io.IOException;

/**
 * Represents a movable object.
 */
public interface Movable {
    /**
     * @param provider an input from user
     * @param model     a current game model
     * @return corresponding move
     * @throws IOException if it occurs
     */
    Move move(UserInputProvider provider, GameModel model) throws IOException;
}
