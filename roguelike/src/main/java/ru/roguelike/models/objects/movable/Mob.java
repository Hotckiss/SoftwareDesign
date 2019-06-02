package ru.roguelike.models.objects.movable;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;

/**
 * Class that represents mob with AI strategy
 */
public class Mob extends AbstractGameParticipant {
    // current mob strategy
    private AbstractStrategy mobStrategy;
    // default mob strategy
    private AbstractStrategy defaultStrategy;

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
        super(position,
                fullHealth,
                physicalDamage,
                fireDamage,
                freezeProbability,
                fireProbability,
                physicalDamageMultiplier,
                fireDamageMultiplier,
                regeneration,
                alias);
        this.isAvailable = false;
        this.defaultStrategy = defaultStrategy;
        this.mobStrategy = defaultStrategy;
        this.experienceCostHit = experienceCostHit;
        this.experienceCostKill = experienceCostKill;
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

    public AbstractStrategy getDefaultStrategy() {
        return defaultStrategy;
    }

    public AbstractStrategy getMobStrategy() {
        return mobStrategy;
    }

    public void setMobStrategy(AbstractStrategy mobStrategy) {
        this.mobStrategy = mobStrategy;
    }
}
