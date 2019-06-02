package ru.roguelike.models.objects.artifacts;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;

/**
 * Represents an element of the game (artifact), which is placed on board and
 * can be collected by player or mob.
 */
public class Artifact extends AbstractGameObject {
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

    public Artifact(Position position,
                    boolean isAvailable,
                    int restoringHealth,
                    int regenerationBonus,
                    double fireProbabilityBonus,
                    double freezeProbabilityBonus,
                    double physicalDamageMultiplierBonus,
                    double fireDamageMultiplierBonus,
                    Character alias) {
        this.position = position;
        this.isAvailable = isAvailable;
        this.restoringHealth = restoringHealth;
        this.regenerationBonus = regenerationBonus;
        this.fireProbabilityBonus = fireProbabilityBonus;
        this.freezeProbabilityBonus = freezeProbabilityBonus;
        this.physicalDamageMultiplierBonus = physicalDamageMultiplierBonus;
        this.fireDamageMultiplierBonus = fireDamageMultiplierBonus;
        this.alias = alias;
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
}
