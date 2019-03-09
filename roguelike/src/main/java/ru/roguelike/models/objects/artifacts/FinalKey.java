package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;

public class FinalKey extends AbstractArtifact {
    public FinalKey(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.restoringHealth = 0;
        this.regenerationBonus = 0;
        this.fireProbabilityBonus = 0;
        this.freezeProbabilityBonus = 0;
        this.physicalDamageMultiplierBonus = 0;
        this.fireDamageMultiplierBonus = 0;
    }

    @Override
    public Character getDrawingFigure() {
        return 'k';
    }
}
