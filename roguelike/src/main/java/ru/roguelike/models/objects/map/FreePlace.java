package ru.roguelike.models.objects.map;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;

import java.io.Serializable;

/**
 * Represents an empty cell on board.
 */
public class FreePlace extends AbstractGameObject implements Serializable {

    /**
     * Constructs new free place on specified position
     * @param position position to add free place
     */
    public FreePlace(Position position) {
        this.position = position;
        this.isAvailable = true;
        this.alias = '.';
    }
}
