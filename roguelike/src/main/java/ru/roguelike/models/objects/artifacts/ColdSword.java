package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;

/**
 * In addition to physical damage can freeze the opponent.
 */
public class ColdSword extends AbstractArtifact {
    public ColdSword(Position position) {
        this.position = position;
        this.isAvailable = true;

        this.restoringHealth = 0;
        this.regenerationBonus = 0;
        this.fireProbabilityBonus = 0;
        this.freezeProbabilityBonus = 0.2;
        this.physicalDamageMultiplierBonus = 0;
        this.fireDamageMultiplierBonus = 0;
    }

    @Override
    public Character getDrawingFigure() {
        return 'c';
    }
}
