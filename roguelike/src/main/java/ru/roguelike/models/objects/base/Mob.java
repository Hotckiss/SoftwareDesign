package ru.roguelike.models.objects.base;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;

/**
 * Class that represents mob with AI strategy
 */
public class Mob extends AbstractGameParticipant {
    // current mob strategy
    protected AbstractStrategy mobStrategy;
    // default mob strategy
    protected AbstractStrategy defaultStrategy;

    private int experienceCostHit;
    private int experienceCostKill;

    public Mob(Position position,
               int fullHealth,
               int physicalDamage,
               int fireDamage,
               double freezeProbability,
               double fireProbability,
               double physicalDamageMultiplier,
               double fireDamageMultiplier,
               int regeneration,
               int experienceCostHit,
               int experienceCostKill,
               AbstractStrategy defaultStrategy,
               Character alias) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = fullHealth;
        this.health = fullHealth;
        this.physicalDamage = physicalDamage;
        this.fireDamage = fireDamage;
        this.freezeProbability = freezeProbability;
        this.fireProbability = fireProbability;
        this.physicalDamageMultiplier = physicalDamageMultiplier;
        this.fireDamageMultiplier = fireDamageMultiplier;
        this.regeneration = regeneration;
        this.freezeCount = 0;
        this.defaultStrategy = defaultStrategy;
        this.experience = 0;
        this.mobStrategy = defaultStrategy;
        this.experienceCostHit = experienceCostHit;
        this.experienceCostKill = experienceCostKill;
        this.alias = alias;
    }

    @Override
    public Move move(UserInputProvider provider, GameModel model) throws IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        return mobStrategy.preferredMove(position, model);
    }

    @Override
    public int getExperience() {
        if (health == 0) {
            return experienceCostKill;
        }

        return experienceCostHit;
    }
}
