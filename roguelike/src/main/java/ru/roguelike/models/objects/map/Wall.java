package ru.roguelike.models.objects.map;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;

/**
 * Represents a wall on board.
 */
public class Wall extends AbstractGameObject {

    public Wall(Position position) {
        this.position = position;
        this.isAvailable = false;
    }

    @Override
    public Character getDrawingFigure() {
        return '#';
    }
}
