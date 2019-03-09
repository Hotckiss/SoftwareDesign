package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;

public class RegenerationPotion extends AbstractArtifact {
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

    @Override
    public Character getDrawingFigure() {
        return 'r';
    }
}
