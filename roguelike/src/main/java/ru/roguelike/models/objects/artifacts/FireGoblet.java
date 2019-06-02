package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.Artifact;

/**
 * Doubles the fire damage of the participant.
 */
public class FireGoblet extends Artifact {
    /**
     * Constructs new fire goblet artifact on specified position
     * @param position artifact position
     */
    public FireGoblet(Position position) {
        this.position = position;
        this.isAvailable = true;

        this.restoringHealth = 0;
        this.regenerationBonus = 0;
        this.fireProbabilityBonus = 0;
        this.freezeProbabilityBonus = 0;
        this.physicalDamageMultiplierBonus = 0;
        this.fireDamageMultiplierBonus = 1.0;
        this.alias = 'g';
    }
}
