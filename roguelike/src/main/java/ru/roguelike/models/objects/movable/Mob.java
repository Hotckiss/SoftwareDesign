package ru.roguelike.models.objects.movable;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.logic.strategies.implementations.RandomStrategy;
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
     * Constructs new mob from builder
     * @param builder builder to extract data
     */
    public Mob(MobBuilder builder) {
        super(builder.position,
                builder.fullHealth,
                builder.physicalDamage,
                builder.fireDamage,
                builder.freezeProbability,
                builder.fireProbability,
                builder.physicalDamageMultiplier,
                builder.fireDamageMultiplier,
                builder.regeneration,
                builder.alias);
        this.isAvailable = false;
        this.defaultStrategy = builder.defaultStrategy;
        this.mobStrategy = builder.mobStrategy;
        this.experienceCostHit = builder.experienceCostHit;
        this.experienceCostKill = builder.experienceCostKill;
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

    /**
     * Mob builder class
     */
    public static class MobBuilder {
        private Position position;
        private Character alias;

        private int fullHealth = 0;
        private AbstractStrategy mobStrategy = new RandomStrategy();
        private AbstractStrategy defaultStrategy = new RandomStrategy();
        private int experienceCostHit = 0;
        private int experienceCostKill = 0;
        private int physicalDamage = 0;
        private int fireDamage = 0;
        private double freezeProbability = 0;
        private double fireProbability = 0;
        private double physicalDamageMultiplier = 1;
        private double fireDamageMultiplier = 1;
        private int regeneration = 0;

        /**
         * Creates new builder with mandatory params
         * @param position mob position
         * @param alias mob alias
         */
        public MobBuilder(Position position, Character alias) {
            this.position = position;
            this.alias = alias;
        }

        /**
         * Add mob health
         * @param fullHealth mob health
         */
        public MobBuilder fullHealth(int fullHealth) {
            this.fullHealth = fullHealth;
            return this;
        }

        /**
         * Add mob exp for hit
         * @param experienceCostHit mob exp hit
         */
        public MobBuilder experienceCostHit(int experienceCostHit) {
            this.experienceCostHit = experienceCostHit;
            return this;
        }

        /**
         * Add mob exp for kill
         * @param experienceCostKill mob exp kill
         */
        public MobBuilder experienceCostKill(int experienceCostKill) {
            this.experienceCostKill = experienceCostKill;
            return this;
        }

        /**
         * Add mob strategy
         * @param defaultStrategy mob default strategy
         */
        public MobBuilder defaultStrategy(AbstractStrategy defaultStrategy) {
            this.defaultStrategy = defaultStrategy;
            this.mobStrategy = defaultStrategy;
            return this;
        }

        /**
         * Add mob physical damage
         * @param physicalDamage mob physical damage
         */
        public MobBuilder physicalDamage(int physicalDamage) {
            this.physicalDamage = physicalDamage;
            return this;
        }

        /**
         * Add mob fire damage
         * @param fireDamage mob fire damage
         */
        public MobBuilder fireDamage(int fireDamage) {
            this.fireDamage = fireDamage;
            return this;
        }

        /**
         * Add mob freeze probability
         * @param freezeProbability mob freeze probability
         */
        public MobBuilder freezeProbability(double freezeProbability) {
            this.freezeProbability = freezeProbability;
            return this;
        }

        /**
         * Add mob fire probability
         * @param fireProbability mob fire probability
         */
        public MobBuilder fireProbability(double fireProbability) {
            this.fireProbability = fireProbability;
            return this;
        }

        /**
         * Add mob physical damage multiplier
         * @param physicalDamageMultiplier mob physical damage multiplier
         */
        public MobBuilder physicalDamageMultiplier(double physicalDamageMultiplier) {
            this.physicalDamageMultiplier = physicalDamageMultiplier;
            return this;
        }

        /**
         * Add mob fire damage multiplier
         * @param fireDamageMultiplier mob fire damage multiplier
         */
        public MobBuilder fireDamageMultiplier(double fireDamageMultiplier) {
            this.fireDamageMultiplier = fireDamageMultiplier;
            return this;
        }

        /**
         * Add mob regeneration
         * @param regeneration mob regeneration
         */
        public MobBuilder regeneration(int regeneration) {
            this.regeneration = regeneration;
            return this;
        }

        /**
         * Create mob with current params
         * @return new mob
         */
        public Mob build() {
            return new Mob(this);
        }
    }
}
