package ru.roguelike.models.objects.movable;

import ru.roguelike.logic.strategies.implementations.PassiveStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractMob;

/**
 * Has less health, but regenerates some part of lost health after damage.
 */
public class Vampire extends AbstractMob {
    /**
     * Constructs new Vampire on specified position
     * @param position position to add Vampire
     */
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
        this.freezeCount = 0;
        this.experience = 0;
        this.defaultStrategy = new PassiveStrategy();
        this.mobStrategy = defaultStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getDrawingFigure() {
        return 'V';
    }

    public int getExperience() {
        if (health == 0) {
            return 5;
        }

        return 0;
    }
}