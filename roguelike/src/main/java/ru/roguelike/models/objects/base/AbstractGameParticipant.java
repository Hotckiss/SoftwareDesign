package ru.roguelike.models.objects.base;

import com.googlecode.lanterna.input.KeyStroke;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Movable;
import ru.roguelike.logic.Move;
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

    public void hit(AbstractGameParticipant opponent) {
        opponent.health = (int) (Math.max(0, opponent.health - physicalDamage * physicalDamageMultiplier));
    }

    public void regenerate() {
        health = Math.min(fullHealth, health + regeneration);
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
        List<Move> availableMoves = new ArrayList<>();
        availableMoves.add(Move.NONE);

        List<List<AbstractGameObject>> field = model.getField();
        int x = position.getX();
        int y = position.getY();

        if (x - 1 >= 0 && availableForMob(field, x - 1, y)) {
            availableMoves.add(Move.TOP);
        }

        if (x + 1 < field.size() && availableForMob(field, x + 1, y)) {
            availableMoves.add(Move.DOWN);
        }

        if (y - 1 >= 0 && availableForMob(field, x, y - 1)) {
            availableMoves.add(Move.LEFT);
        }

        if (y + 1 < field.get(x).size() && availableForMob(field, x, y + 1)) {
            availableMoves.add(Move.RIGHT);
        }

        Random random = new Random();

        return availableMoves.get(random.nextInt(availableMoves.size()));
    }

    private boolean availableForMob(@NotNull List<List<AbstractGameObject>> field,
                                    int x, int y) {
        return !(field.get(x).get(y) instanceof Wall || field.get(x).get(y) instanceof AbstractArtifact);
    }
}
