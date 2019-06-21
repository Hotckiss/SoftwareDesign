package ru.roguelike.models.objects.map;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;

import java.io.Serializable;

/**
 * Represents a wall on board.
 */
public class Wall extends AbstractGameObject implements Serializable {

    /**
     * Constructs new wall on specified position
     * @param position position to add wall
     */
    public Wall(Position position) {
        this.position = position;
        this.isAvailable = false;
        this.alias = '#';
    }
}
