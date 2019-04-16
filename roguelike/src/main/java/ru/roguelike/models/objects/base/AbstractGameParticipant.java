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
        return health > 0;
    }

    public int getHealth() {
        return health;
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
