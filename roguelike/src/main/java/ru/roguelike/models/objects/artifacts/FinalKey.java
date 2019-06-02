package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.Artifact;

/**
 * If the player collects this item, he wins.
 */
public class FinalKey extends Artifact {
    /**
     * Constructs new final key artifact on specified position
     * @param position artifact position
     */
    public FinalKey(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.restoringHealth = 0;
        this.regenerationBonus = 0;
        this.fireProbabilityBonus = 0;
        this.freezeProbabilityBonus = 0;
        this.physicalDamageMultiplierBonus = 0;
        this.fireDamageMultiplierBonus = 0;
        this.alias = 'k';
    }
}
