package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;

import java.io.Serializable;

/**
 * If the player collects this item, he wins.
 */
public class FinalKey extends Artifact implements Serializable {
    /**
     * Constructs new final key artifact on specified position
     * @param position artifact position
     */
    public FinalKey(Position position) {
        super(position, false, 0, 0, 0, 0, 0, 0, 'k');
    }
}
