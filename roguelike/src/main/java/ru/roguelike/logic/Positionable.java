package ru.roguelike.logic;

import ru.roguelike.models.Position;

public interface Positionable {
    Position getPosition();
    void setPosition(Position newPosition);
}
