package ru.roguelike.models.objects.movable;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;

public class Flier extends AbstractGameParticipant {
    public Flier(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 10;
        this.health = 10;
        this.physicalDamage = 20;
        this.fireDamage = 0;
        this.freezeProbability = 0;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 0;
    }

    @Override
    public Character getDrawingFigure() {
        return 'F';
    }
}