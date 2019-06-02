package ru.roguelike.models.objects.movable;

import ru.roguelike.logic.strategies.implementations.RandomStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractMob;

/**
 * Represents a simple mob.
 */
public class SimpleMob extends AbstractMob {
    /**
     * Constructs new SimpleMob on specified position
     * @param position position to add SimpleMob
     */
    public SimpleMob(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 50;
        this.health = 50;
        this.physicalDamage = 20;
        this.fireDamage = 0;
        this.freezeProbability = 0;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 0;
        this.freezeCount = 0;
        this.experience = 0;
        this.defaultStrategy = new RandomStrategy();
        this.mobStrategy = defaultStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getDrawingFigure() {
        return 'S';
    }

    public int getExperience() {
        if (health == 0) {
            return 15;
        }

        return 2;
    }
}
