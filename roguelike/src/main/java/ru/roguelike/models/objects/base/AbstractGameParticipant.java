package ru.roguelike.models.objects.base;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.ExpirienceProvider;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Movable;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.ConfusedStrategyDecorator;
import ru.roguelike.logic.strategies.implementations.RandomStrategy;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;

/**
 * Represents participant of the game (in current implementation it can
 * player or mob).
 */
public abstract class AbstractGameParticipant extends AbstractGameObject implements Movable, ExpirienceProvider {
    /**
     * Full health of participant
     */
    protected int fullHealth;
    /**
     * Current health of participant
     */
    protected int health;
    /**
     * Physical damage of participant
     */
    protected int physicalDamage;
    /**
     * Fire damage of participant
     */
    protected int fireDamage;
    /**
     * Freeze chance of participant
     */
    protected double freezeProbability;
    /**
     * Fire chance of participant
     */
    protected double fireProbability;
    /**
     * Physical damage multiplier of participant
     */
    protected double physicalDamageMultiplier;
    /**
     * Fire damage multiplier of participant
     */
    protected double fireDamageMultiplier;
    /**
     * Current health regeneration of participant each turn
     */
    protected int regeneration;
    /**
     * Freezed turns count
     */
    protected int freezeCount;
    /**
     * Fired turns count
     */
    protected int fireCount;
    /**
     * Damage taken from fire each turn
     */
    protected int fireValue;

    /**
     * Expirience of game participant
     */
    protected int experience;

    /**
     * Method that hits opponent
     * @param opponent opponent to hit
     */
    public void hit(AbstractGameParticipant opponent) {
        opponent.health = (int) (Math.max(0, opponent.health - physicalDamage * physicalDamageMultiplier * (1 + Math.abs(getLevel() - 1) * 0.5)));
        // opponent freezed
        if (Math.random() < freezeProbability) {
            opponent.freezeCount = 3;
        }
        // opponent fired
        if (Math.random() < fireProbability) {
            opponent.fireCount = 3;
            opponent.fireValue = (int)(fireDamageMultiplier * fireDamage);
        }

        if (opponent instanceof AbstractMob) {
            AbstractMob mob = (AbstractMob)opponent;
            mob.mobStrategy = new ConfusedStrategyDecorator(mob.defaultStrategy, 3);
        }

        experience += opponent.getExperience();
    }

    /**
     * Method that regenerates participant after turn
     */
    public void regenerate() {
        health = Math.min(fullHealth, health + regeneration);
    }

    /**
     * Method that decrease freeze of player
     */
    public void freezeStep() {
        freezeCount = Math.max(0, freezeCount - 1);
    }

    /**
     * Method that decrease fire health of player
     */
    public void fireStep() {
        if (fireCount > 0) {
            health = (int) (Math.max(0, health - fireValue));
            fireCount--;
        }
    }

    /**
     * Indicates that participant is alive
     * @return true if participant is alive false otherwise
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Method to get participant health
     * @return current health of participant
     */
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }

    public int getFullHealth() {
        return fullHealth;
    }
    public void setFullHealth(int fullHealth) {
        this.fullHealth = fullHealth;
    }

    public int getPhysicalDamage() {
        return physicalDamage;
    }
    public void setPhysicalDamage(int physicalDamage) {
        this.physicalDamage = physicalDamage;
    }

    public int getFireDamage() {
        return fireDamage;
    }
    public void setFireDamage(int fireDamage) {
        this.fireDamage = fireDamage;
    }

    public double getFreezeProbability() {
        return freezeProbability;
    }
    public void setFreezeProbability(double freezeProbability) {
        this.freezeProbability = freezeProbability;
    }

    public double getFireProbability() {
        return fireProbability;
    }
    public void setFireProbability(double fireProbability) {
        this.fireProbability = fireProbability;
    }

    public double getPhysicalDamageMultiplier() {
        return physicalDamageMultiplier;
    }
    public void setPhysicalDamageMultiplier(double physicalDamageMultiplier) {
        this.physicalDamageMultiplier = physicalDamageMultiplier;
    }

    public double getFireDamageMultiplier() {
        return fireDamageMultiplier;
    }
    public void setFireDamageMultiplier(double fireDamageMultiplier) {
        this.fireDamageMultiplier = fireDamageMultiplier;
    }

    public int getRegeneration() {
        return regeneration;
    }
    public void setRegeneration(int regeneration) {
        this.regeneration = regeneration;
    }

    public int getFreezeCount() {
        return freezeCount;
    }
    public void setFreezeCount(int freezeCount) {
        this.freezeCount = freezeCount;
    }

    public int getFireCount() {
        return fireCount;
    }
    public void setFireCount(int fireCount) {
        this.fireCount = fireCount;
    }

    public int getFireValue() {
        return fireValue;
    }
    public void setFireValue(int fireValue) {
        this.fireValue = fireValue;
    }

    public int exp() {
        return experience;
    }

    public void setExperience(int newExp) {
        experience = newExp;
    }

    /**
     * Collects a artifact.
     *
     * @param artifact is an artifact to be collected.
     */
    public void enableArtifact(@NotNull AbstractArtifact artifact) {
        health = Math.min(fullHealth, health + artifact.restoringHealth);
        fireDamageMultiplier += artifact.fireDamageMultiplierBonus;
        fireProbability += artifact.fireProbabilityBonus;
        freezeProbability += artifact.freezeProbabilityBonus;
        physicalDamageMultiplier += artifact.physicalDamageMultiplierBonus;
        regeneration += artifact.regenerationBonus;
    }

    /**
     * Disables an artifact (loses it).
     *
     * @param artifact is an artifact to be disabled.
     */
    public void disableArtifact(@NotNull AbstractArtifact artifact) {
        fireDamageMultiplier -= artifact.fireDamageMultiplierBonus;
        fireProbability -= artifact.fireProbabilityBonus;
        freezeProbability -= artifact.freezeProbabilityBonus;
        physicalDamageMultiplier -= artifact.physicalDamageMultiplierBonus;
        regeneration -= artifact.regenerationBonus;
    }

    /**
     * Returns preferred move for participant
     * @param provider an input from user
     * @param model a current game model
     * @return preferred move
     * @throws IOException if can not read move from input
     */
    @Override
    public Move move(UserInputProvider provider, GameModel model) throws IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        return new RandomStrategy().preferredMove(position, model);
    }

    public int getLevel() {
        return experience / 50 + 1;
    }
}
