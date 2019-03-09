package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;

//TODO: multi-movement logic
public class SpeedBoots extends AbstractArtifact {
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

    @Override
    public Character getDrawingFigure() {
        return 's';
    }
}
