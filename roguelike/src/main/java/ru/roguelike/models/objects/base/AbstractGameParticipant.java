package ru.roguelike.models.objects.base;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.ExpirienceProvider;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Movable;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.logic.strategies.ConfusedStrategyDecorator;
import ru.roguelike.logic.strategies.implementations.RandomStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.movable.Mob;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;
import java.io.Serializable;

/**
 * Represents participant of the game (in current implementation it can
 * player or mob).
 */
public class AbstractGameParticipant extends AbstractGameObject implements Movable, ExpirienceProvider, Serializable {
    /**
     * Full health of participant
     */
    private int fullHealth;
    /**
     * Current health of participant
     */
    private int health;
    /**
     * Physical damage of participant
     */
    private int physicalDamage;
    /**
     * Fire damage of participant
     */
    private int fireDamage;
    /**
     * Freeze chance of participant
     */
    private double freezeProbability;
    /**
     * Fire chance of participant
     */
    private double fireProbability;
    /**
     * Physical damage multiplier of participant
     */
    private double physicalDamageMultiplier;
    /**
     * Fire damage multiplier of participant
     */
    private double fireDamageMultiplier;
    /**
     * Current health regeneration of participant each turn
     */
    private int regeneration;
    /**
     * Freeze turns count
     */
    protected int freezeCount;
    /**
     * Fired turns count
     */
    private int fireCount;
    /**
     * Damage taken from fire each turn
     */
    private int fireValue;

    /**
     * Experience of game participant
     */
    private int experience;

    public AbstractGameParticipant(Position position,
                                   int fullHealth,
                                   int physicalDamage,
                                   int fireDamage,
                                   double freezeProbability,
                                   double fireProbability,
                                   double physicalDamageMultiplier,
                                   double fireDamageMultiplier,
                                   int regeneration,
                                   Character alias) {
        this.position = position;
        this.fullHealth = fullHealth;
        this.health = fullHealth;
        this.physicalDamage = physicalDamage;
        this.fireDamage = fireDamage;
        this.freezeProbability = freezeProbability;
        this.fireProbability = fireProbability;
        this.physicalDamageMultiplier = physicalDamageMultiplier;
        this.fireDamageMultiplier = fireDamageMultiplier;
        this.regeneration = regeneration;
        this.alias = alias;
        this.freezeCount = 0;
        this.experience = 0;
    }
    /**
     * Method that hits opponent
     * @param opponent opponent to hit
     */
    public void hit(AbstractGameParticipant opponent) {
        opponent.health = (int) (Math.max(0, opponent.health - physicalDamage * physicalDamageMultiplier * (1 + Math.abs(getLevel() - 1) * 0.5)));
        // opponent freeze
        if (Math.random() < freezeProbability) {
            opponent.freezeCount = 3;
        }
        // opponent fired
        if (Math.random() < fireProbability) {
            opponent.fireCount = 3;
            opponent.fireValue = (int)(fireDamageMultiplier * fireDamage);
        }

        if (opponent instanceof Mob) {
            Mob mob = (Mob)opponent;
            mob.setMobStrategy(new ConfusedStrategyDecorator(mob.getDefaultStrategy(), 3));
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

    public void setExperience(int newExp) {
        experience = newExp;
    }

    /**
     * Collects a artifact.
     *
     * @param artifact is an artifact to be collected.
     */
    public void enableArtifact(@NotNull Artifact artifact) {
        health = Math.min(fullHealth, health + artifact.getRestoringHealth());
        fireDamageMultiplier += artifact.getFireDamageMultiplierBonus();
        fireProbability += artifact.getFireProbabilityBonus();
        freezeProbability += artifact.getFreezeProbabilityBonus();
        physicalDamageMultiplier += artifact.getPhysicalDamageMultiplierBonus();
        regeneration += artifact.getRegenerationBonus();
    }

    /**
     * Disables an artifact (loses it).
     *
     * @param artifact is an artifact to be disabled.
     */
    public void disableArtifact(@NotNull Artifact artifact) {
        fireDamageMultiplier -= artifact.getFireDamageMultiplierBonus();
        fireProbability -= artifact.getFireProbabilityBonus();
        freezeProbability -= artifact.getFreezeProbabilityBonus();
        physicalDamageMultiplier -= artifact.getPhysicalDamageMultiplierBonus();
        regeneration -= artifact.getRegenerationBonus();
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

    @Override
    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return experience / 50 + 1;
    }
}
