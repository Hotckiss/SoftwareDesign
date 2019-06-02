package ru.roguelike.models.objects.base;

/**
 * Represents an element of the game (artifact), which is placed on board and
 * can be collected by player or mob.
 */
public class Artifact extends AbstractGameObject {
    /**
     * Health restore value of artifact
     */
    protected int restoringHealth;
    /**
     * Regeneration value of artifact each turn
     */
    protected int regenerationBonus;
    /**
     * Fire chance improvement value
     */
    protected double fireProbabilityBonus;
    /**
     * Cold chance improvement value
     */
    protected double freezeProbabilityBonus;
    /**
     * Physical damage improvement multiplier value
     */
    protected double physicalDamageMultiplierBonus;
    /**
     * Fire damage improvement multiplier value
     */
    protected double fireDamageMultiplierBonus;

    /**
     * Fire damage improvement multiplier value
     */
    protected Character alias;
    /**
     * Flag of taking this artifact
     */
    protected boolean isTaken = false;

    public Artifact(int restoringHealth,
                    int regenerationBonus,
                    double fireProbabilityBonus,
                    double freezeProbabilityBonus,
                    double physicalDamageMultiplierBonus,
                    double fireDamageMultiplierBonus,
                    Character alias) {
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
}
