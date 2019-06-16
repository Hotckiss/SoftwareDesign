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

        public ArtifactBuilder(Position position,
                               boolean isAvailable,
                               Character alias) {
            this.position = position;
            this.isAvailable = isAvailable;
            this.alias = alias;
        }

        public ArtifactBuilder restoringHealth(int restoringHealth) {
            this.restoringHealth = restoringHealth;
            return this;
        }

        public ArtifactBuilder regenerationBonus(int regenerationBonus) {
            this.regenerationBonus = regenerationBonus;
            return this;
        }

        public ArtifactBuilder fireProbabilityBonus(double fireProbabilityBonus) {
            this.fireProbabilityBonus = fireProbabilityBonus;
            return this;
        }

        public ArtifactBuilder freezeProbabilityBonus(double freezeProbabilityBonus) {
            this.freezeProbabilityBonus = freezeProbabilityBonus;
            return this;
        }

        public ArtifactBuilder physicalDamageMultiplierBonus(double physicalDamageMultiplierBonus) {
            this.physicalDamageMultiplierBonus = physicalDamageMultiplierBonus;
            return this;
        }

        public ArtifactBuilder fireDamageMultiplierBonus(double fireDamageMultiplierBonus) {
            this.fireDamageMultiplierBonus = fireDamageMultiplierBonus;
            return this;
        }

        public Artifact build() {
            return new Artifact(this);
        }
    }
}
