package ru.roguelike.models.objects.map;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;

/**
 * Represents an empty cell on board.
 */
public class FreePlace extends AbstractGameObject {

    public FreePlace(Position position) {
        this.position = position;
        this.isAvailable = true;
    }

    @Override
    public Character getDrawingFigure() {
        return '.';
    }
}
