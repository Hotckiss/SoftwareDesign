package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.Artifact;

/**
 * In addition to physical damage can freeze the opponent.
 */
public class ColdSword extends Artifact {
    /**
     * Constructs new cold sword artifact on specified position
     * @param position artifact position
     */
    public ColdSword(Position position) {
        this.position = position;
        this.isAvailable = true;

        this.restoringHealth = 0;
        this.regenerationBonus = 0;
        this.fireProbabilityBonus = 0;
        this.freezeProbabilityBonus = 0.2;
        this.physicalDamageMultiplierBonus = 0;
        this.fireDamageMultiplierBonus = 0;
        this.alias = 'c';
    }
}
