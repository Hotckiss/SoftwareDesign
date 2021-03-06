package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;

import java.io.Serializable;

/**
 * Represents an element of the game (artifact), which is placed on board and
 * can be collected by player or mob.
 */
public class Artifact extends AbstractGameObject implements Serializable {
    /**
     * Health restore value of artifact
     */
    private int restoringHealth;
    /**
     * Regeneration value of artifact each turn
     */
    private int regenerationBonus;
    /**
     * Fire chance improvement value
     */
    private double fireProbabilityBonus;
    /**
     * Cold chance improvement value
     */
    private double freezeProbabilityBonus;
    /**
     * Physical damage improvement multiplier value
     */
    private double physicalDamageMultiplierBonus;
    /**
     * Fire damage improvement multiplier value
     */
    private double fireDamageMultiplierBonus;

    /**
     * Flag of taking this artifact
     */
    private boolean isTaken = false;

    /**
     * Constructs new artifact with input params
     */
    public Artifact(Position position,
                    boolean isAvailable,
                    Character alias) {
        this.position = position;
        this.isAvailable = isAvailable;
        this.restoringHealth = 0;
        this.regenerationBonus = 0;
        this.fireProbabilityBonus = 0;
        this.freezeProbabilityBonus = 0;
        this.physicalDamageMultiplierBonus = 0;
        this.fireDamageMultiplierBonus = 0;
        this.alias = alias;
    }

    /**
     * Constructs new artifact with input builder
     */
    public Artifact(ArtifactBuilder builder) {
        this.position = builder.position;
        this.isAvailable = builder.isAvailable;
        this.restoringHealth = builder.restoringHealth;
        this.regenerationBonus = builder.regenerationBonus;
        this.fireProbabilityBonus = builder.fireProbabilityBonus;
        this.freezeProbabilityBonus = builder.freezeProbabilityBonus;
        this.physicalDamageMultiplierBonus = builder.physicalDamageMultiplierBonus;
        this.fireDamageMultiplierBonus = builder.fireDamageMultiplierBonus;
        this.alias = builder.alias;
    }

    /**
     * Takes this artifact on player
     */
    public void take() {
        isTaken = true;
    }

    /**
     * Indicates that this artifact is taken
     * @return true if artifact is taken false otherwise
     */
    public boolean taken() {
        return isTaken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getDrawingFigure() {
        return alias;
    }

    public int getRestoringHealth() {
        return restoringHealth;
    }

    public int getRegenerationBonus() {
        return regenerationBonus;
    }

    public double getFireProbabilityBonus() {
        return fireProbabilityBonus;
    }

    public double getFreezeProbabilityBonus() {
        return freezeProbabilityBonus;
    }

    public double getPhysicalDamageMultiplierBonus() {
        return physicalDamageMultiplierBonus;
    }

    public double getFireDamageMultiplierBonus() {
        return fireDamageMultiplierBonus;
    }

    /**
     * Builder for artifacts
     */
    public static class ArtifactBuilder {
        private Position position;
        private boolean isAvailable;
        private Character alias;
        private int restoringHealth = 0;
        private int regenerationBonus = 0;
        private double fireProbabilityBonus = 0;
        private double freezeProbabilityBonus = 0;
        private double physicalDamageMultiplierBonus = 0;
        private double fireDamageMultiplierBonus = 0;

        /**
         * Creates new builder with mandatory params
         * @param position artifact position
         * @param alias artifact alias
         */
        public ArtifactBuilder(Position position,
                               boolean isAvailable,
                               Character alias) {
            this.position = position;
            this.isAvailable = isAvailable;
            this.alias = alias;
        }

        /**
         * Add artifact health restore
         * @param restoringHealth artifact health restore
         */
        public ArtifactBuilder restoringHealth(int restoringHealth) {
            this.restoringHealth = restoringHealth;
            return this;
        }

        /**
         * Add artifact regeneration bonus
         * @param regenerationBonus artifact regeneration
         */
        public ArtifactBuilder regenerationBonus(int regenerationBonus) {
            this.regenerationBonus = regenerationBonus;
            return this;
        }

        /**
         * Add artifact fire probability bonus
         * @param fireProbabilityBonus artifact fire probability bonus
         */
        public ArtifactBuilder fireProbabilityBonus(double fireProbabilityBonus) {
            this.fireProbabilityBonus = fireProbabilityBonus;
            return this;
        }

        /**
         * Add artifact freeze probability bonus
         * @param freezeProbabilityBonus artifact freeze probability bonus
         */
        public ArtifactBuilder freezeProbabilityBonus(double freezeProbabilityBonus) {
            this.freezeProbabilityBonus = freezeProbabilityBonus;
            return this;
        }

        /**
         * Add artifact physical damage multiplier bonus
         * @param physicalDamageMultiplierBonus artifact physical damage multiplier bonus
         */
        public ArtifactBuilder physicalDamageMultiplierBonus(double physicalDamageMultiplierBonus) {
            this.physicalDamageMultiplierBonus = physicalDamageMultiplierBonus;
            return this;
        }

        /**
         * Add artifact fire damage multiplier bonus
         * @param fireDamageMultiplierBonus artifact fire damage multiplier bonus
         */
        public ArtifactBuilder fireDamageMultiplierBonus(double fireDamageMultiplierBonus) {
            this.fireDamageMultiplierBonus = fireDamageMultiplierBonus;
            return this;
        }

        /**
         * Create artifact with current params
         * @return new artifact
         */
        public Artifact build() {
            return new Artifact(this);
        }
    }
}
