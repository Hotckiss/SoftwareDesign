package ru.roguelike.models.objects.movable;

import ru.roguelike.logic.strategies.implementations.AggressiveStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractMob;

/**
 * Simple monster with low physical damage which who has a higher probability
 * to do freeze damage.
 */
public class Yeti extends AbstractMob {
    /**
     * Constructs new Yeti on specified position
     * @param position position to add Yeti
     */
    public Yeti(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 50;
        this.health = 50;
        this.physicalDamage = 15;
        this.fireDamage = 0;
        this.freezeProbability = 0.4;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 0;
        this.freezeCount = 0;
        this.experience = 0;
        this.defaultStrategy = new AggressiveStrategy();
        this.mobStrategy = defaultStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getDrawingFigure() {
        return 'Y';
    }

    public int getExperience() {
        if (health == 0) {
            return 20;
        }

        return 6;
    }
}