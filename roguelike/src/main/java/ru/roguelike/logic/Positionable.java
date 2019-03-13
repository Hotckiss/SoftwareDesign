package ru.roguelike.logic;

import ru.roguelike.models.Position;

/**
 * Represents an object which can be placed on board.
 */
public interface Positionable {
    Position getPosition();

    void setPosition(Position newPosition);
}
