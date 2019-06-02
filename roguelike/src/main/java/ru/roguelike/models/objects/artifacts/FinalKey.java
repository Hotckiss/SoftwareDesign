package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;

/**
 * If the player collects this item, he wins.
 */
public class FinalKey extends Artifact {
    /**
     * Constructs new final key artifact on specified position
     * @param position artifact position
     */
    public FinalKey(Position position) {
        super(position, false, 0, 0, 0, 0, 0, 0, 'k');
    }
}
