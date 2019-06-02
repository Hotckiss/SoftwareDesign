package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;

/**
 * Regenerates the health and gives constant ability
 * of regeneration.
 */
public class RegenerationPotion extends AbstractArtifact {
    /**
     * Constructs new regeneration potion artifact on specified position
     * @param position artifact position
     */
    public RegenerationPotion(Position position) {
        this.position = position;
        this.isAvailable = true;

        this.restoringHealth = 30;
        this.regenerationBonus = 5;
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
        return 'r';
    }
}
