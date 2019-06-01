package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;

/**
 * Regenerates a health of the participant.
 */
public class HealthWater extends AbstractArtifact {
    /**
     * Constructs new health water artifact on specified position
     * @param position artifact position
     */
    public HealthWater(Position position) {
        this.position = position;
        this.isAvailable = true;

        this.restoringHealth = 50;
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
        return 'h';
    }
}
