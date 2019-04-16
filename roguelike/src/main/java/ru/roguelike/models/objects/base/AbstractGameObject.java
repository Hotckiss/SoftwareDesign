package ru.roguelike.models.objects.base;

import ru.roguelike.logic.Positionable;
import ru.roguelike.models.Position;
import ru.roguelike.view.Drawable;

/**
 * Represents an element which is present in the game.
 */
public abstract class AbstractGameObject implements Drawable, Positionable {
    // позиция на поле
    protected Position position;
    protected boolean isAvailable;

    /**
     * {@inheritDoc}
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(Position newPosition) {
        position = newPosition;
    }

    /**
     * @return if the object is available.
     */
    public boolean available() {
        return isAvailable;
    }
}