package ru.roguelike.models.objects.movable;

import com.googlecode.lanterna.input.KeyStroke;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.logic.strategies.implementations.AggressiveStrategy;
import ru.roguelike.logic.strategies.implementations.CowardStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractMob;

import java.io.IOException;

/**
 * Mob which is able to make to strokes in one move.
 * Has less help than simple mob.
 */
public class Flier extends AbstractMob {
    /**
     * Constructs new Flier on specified position
     * @param position position to add Flier
     */
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
        this.freezeCount = 0;
        this.defaultStrategy = new CowardStrategy();
        this.experience = 0;
        this.mobStrategy = defaultStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getDrawingFigure() {
        return 'F';
    }

    public int getExperience() {
        if (health == 0) {
            return 5;
        }

        return 1;
    }
}