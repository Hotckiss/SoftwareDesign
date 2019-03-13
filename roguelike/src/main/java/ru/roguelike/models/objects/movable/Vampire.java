package ru.roguelike.models.objects.movable;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;

/**
 * Has less health, but regenerates some part of lost health after damage.
 */
public class Vampire extends AbstractGameParticipant {
    public Vampire(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 40;
        this.health = 40;
        this.physicalDamage = 10;
        this.fireDamage = 0;
        this.freezeProbability = 0;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 10;
    }

    @Override
    public Character getDrawingFigure() {
        return 'V';
    }
}