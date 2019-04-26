package ru.roguelike.models.objects.base;

import com.googlecode.lanterna.input.KeyStroke;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Movable;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.ConfusedStrategyDecorator;
import ru.roguelike.logic.strategies.implementations.RandomStrategy;
import ru.roguelike.models.objects.map.Wall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents participant of the game (in current implementation it can
 * player or mob).
 */
public abstract class AbstractGameParticipant extends AbstractGameObject implements Movable {
    //фулл хп
    protected int fullHealth;
    //остаток хп
    protected int health;
    //урон обычный
    protected int physicalDamage;
    //урон огнем в ход при нанесении
    protected int fireDamage;
    //шанс заморозить
    protected double freezeProbability;
    //шанс поджечь
    protected double fireProbability;
    // множитель физ урона
    protected double physicalDamageMultiplier;
    //множитель огненного урона
    protected double fireDamageMultiplier;
    //восстанавливает хп в ход
    protected int regeneration;
    //сколько ходов еще заморожен
    protected int freezeCount;
    //сколько ходов еще горит
    protected int fireCount;
    //сколько получает огненного урона
    protected int fireValue;

    public void hit(AbstractGameParticipant opponent) {
        Random random = new Random();

        opponent.health = (int) (Math.max(0, opponent.health - physicalDamage * physicalDamageMultiplier));
        // opponent freezed
        if (random.nextDouble() < freezeProbability) {
            opponent.freezeCount = 3;
        }
        // opponent fired
        if (random.nextDouble() < fireProbability) {
            opponent.fireCount = 3;
            opponent.fireValue = (int)(fireDamageMultiplier * fireDamage);
        }

        if (opponent instanceof AbstractMob) {
            AbstractMob mob = (AbstractMob)opponent;
            mob.mobStrategy = new ConfusedStrategyDecorator(mob.defaultStrategy, 3);
        }
    }

    public void regenerate() {
        health = Math.min(fullHealth, health + regeneration);
    }

    public void freezeStep() {
        freezeCount = Math.max(0, freezeCount - 1);
    }

    public void fireStep() {
        if (fireCount > 0) {
            health = (int) (Math.max(0, health - fireValue));
            fireCount--;
        }
    }

    public boolean isAlive() {
        System.out.println("HEALTH = " + health);
        return health > 0;
    }

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

    @Override
    public Move move(KeyStroke keyStroke, GameModel model) throws IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        return new RandomStrategy().preferredMove(position, model);
    }
}
