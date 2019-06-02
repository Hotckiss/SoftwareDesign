package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;

//TODO: multi-movement logic

/**
 * Allows to make two actions in one move.
 */
public class SpeedBoots extends AbstractArtifact {
    /**
     * Constructs new speed boots artifact on specified position
     * @param position artifact position
     */
    public SpeedBoots(Position position) {
        this.position = position;
        this.isAvailable = true;

        this.restoringHealth = 0;
        this.regenerationBonus = 0;
        this.fireProbabilityBonus = 0;
        this.freezeProbabilityBonus = 0;
        this.physicalDamageMultiplierBonus = 0;
        this.fireDamageMultiplierBonus = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getDrawingFigure() {
        return 's';
    }
}
