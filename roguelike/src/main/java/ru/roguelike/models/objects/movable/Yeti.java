package ru.roguelike.models.objects.movable;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;

/**
 * Simple monster with low physical damage which who has a higher probability
 * to do freeze damage.
 */
public class Yeti extends AbstractGameParticipant {
    public Yeti(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 50;
        this.health = 50;
        this.physicalDamage = 15;
        this.fireDamage = 0;
        this.freezeProbability = 0.3;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 0;
    }

    @Override
    public Character getDrawingFigure() {
        return 'Y';
    }
}