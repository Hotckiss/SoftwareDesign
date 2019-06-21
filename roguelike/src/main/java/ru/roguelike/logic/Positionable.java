package ru.roguelike.logic;

import ru.roguelike.models.Position;

/**
 * Represents an object which can be placed on board.
 */
public interface Positionable {
    /**
     * @return a position of the object.
     */
    Position getPosition();

    /**
     * @param newPosition is a new position to be set.
     */
    void setPosition(Position newPosition);
}
