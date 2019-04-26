package ru.roguelike.models.objects.movable;

import com.googlecode.lanterna.input.KeyStroke;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.logic.strategies.implementations.AggressiveStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.base.AbstractMob;

import java.io.IOException;

/**
 * Simple monster with low physical damage which who has a higher probability
 * to do freeze damage.
 */
public class Yeti extends AbstractMob {
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