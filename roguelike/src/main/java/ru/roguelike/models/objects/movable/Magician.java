package ru.roguelike.models.objects.movable;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;

/**
 * Has less health but can regenerate quickly.
 * Has small physical damage, but can strike only with fire.
 */
public class Magician extends AbstractGameParticipant {
    public Magician(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 20;
        this.health = 20;
        this.physicalDamage = 40;
        this.fireDamage = 10;
        this.freezeProbability = 0;
        this.fireProbability = 1;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 15;
    }

    @Override
    public Character getDrawingFigure() {
        return 'M';
    }
}