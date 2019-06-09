package ru.roguelike.models.objects.movable;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class that represents mob with AI strategy
 */
public class Mob extends AbstractGameParticipant implements Serializable {
    // current mob strategy
    private AbstractStrategy mobStrategy;
    // default mob strategy
    private AbstractStrategy defaultStrategy;

    private int experienceCostHit;
    private int experienceCostKill;

    /**
     * Constructs new mob
     * @param position mob position
     * @param fullHealth mob max health
     * @param physicalDamage mob damage
     * @param fireDamage mob fire damage
     * @param freezeProbability mob freezeProbability
     * @param fireProbability mob fireProbability
     * @param physicalDamageMultiplier mob physicalDamageMultiplier
     * @param fireDamageMultiplier mob fireDamageMultiplier mob
     * @param regeneration mob regeneration
     * @param experienceCostHit experience for hitting mob
     * @param experienceCostKill experience for killing mob
     * @param defaultStrategy mob defaultStrategy
     * @param alias mob drawing alias
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Move move(UserInputProvider provider, GameModel model) throws IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        return mobStrategy.preferredMove(position, model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getExperience() {
        if (!isAlive()) {
            return experienceCostKill;
        }

        return experienceCostHit;
    }

    /**
     * Getter for default mob strategy
     * @return default mob strategy
     */
    public AbstractStrategy getDefaultStrategy() {
        return defaultStrategy;
    }

    /**
     * Getter for current mob strategy
     * @return current mob strategy
     */
    public AbstractStrategy getMobStrategy() {
        return mobStrategy;
    }

    /**
     * Setter for current mob strategy. Used for confusing mobs
     * @param mobStrategy new mob strategy
     */
    public void setMobStrategy(AbstractStrategy mobStrategy) {
        this.mobStrategy = mobStrategy;
    }
}
