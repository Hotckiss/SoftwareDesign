package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;

/**
 * Doubles the fire damage of the participant.
 */
public class FireGoblet extends AbstractArtifact {
    public FireGoblet(Position position) {
        this.position = position;
        this.isAvailable = true;

        this.restoringHealth = 0;
        this.regenerationBonus = 0;
        this.fireProbabilityBonus = 0;
        this.freezeProbabilityBonus = 0;
        this.physicalDamageMultiplierBonus = 0;
        this.fireDamageMultiplierBonus = 1.0;
    }

    @Override
    public Character getDrawingFigure() {
        return 'g';
    }
}
