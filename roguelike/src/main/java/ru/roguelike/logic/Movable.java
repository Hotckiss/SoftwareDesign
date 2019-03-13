package ru.roguelike.logic;

import com.googlecode.lanterna.input.KeyStroke;

import java.io.IOException;

/**
 * Represents a movable object.
 */
public interface Movable {
    /**
     * @param keyStroke an input from user
     * @param model     a current game model
     * @return corresponding move
     * @throws IOException if it occurs
     */
    Move move(KeyStroke keyStroke, GameModel model) throws IOException;
}
