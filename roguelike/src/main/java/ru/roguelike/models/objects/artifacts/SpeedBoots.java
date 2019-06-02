package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.Artifact;

//TODO: multi-movement logic

/**
 * Allows to make two actions in one move.
 */
public class SpeedBoots extends Artifact {
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
        this.alias = 's';
    }
}
