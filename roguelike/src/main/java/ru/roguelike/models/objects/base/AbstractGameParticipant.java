package ru.roguelike.models.objects.base;

import com.googlecode.lanterna.screen.Screen;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Movable;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.base.AbstractGameObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        opponent.health = (int)(Math.max(0, opponent.health - physicalDamage * physicalDamageMultiplier));
    }

    public void regenerate() {
        health = Math.min(fullHealth, health + regeneration);
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void enableArtifact(AbstractArtifact artifact) {
        health = Math.min(fullHealth, health + artifact.restoringHealth);
        fireDamageMultiplier += artifact.fireDamageMultiplierBonus;
        fireProbability += artifact.fireProbabilityBonus;
        freezeProbability += artifact.freezeProbabilityBonus;
        physicalDamageMultiplier += artifact.physicalDamageMultiplierBonus;
        regeneration += artifact.regenerationBonus;
    }

    public void disableArtifact(AbstractArtifact artifact) {
        fireDamageMultiplier -= artifact.fireDamageMultiplierBonus;
        fireProbability -= artifact.fireProbabilityBonus;
        freezeProbability -= artifact.freezeProbabilityBonus;
        physicalDamageMultiplier -= artifact.physicalDamageMultiplierBonus;
        regeneration -= artifact.regenerationBonus;
    }

    @Override
    public Move move(Screen screen, GameModel model) throws IOException {
        List<Move> availableMoves = new ArrayList<>();
        availableMoves.add(Move.NONE);

        List<List<AbstractGameObject>> field = model.getField();
        int x = position.getX();
        int y = position.getY();

        if (x - 1 >= 0 && field.get(x - 1).get(y).isAvailable) {
            availableMoves.add(Move.TOP);
        }

        if (x + 1 < field.size() && field.get(x + 1).get(y).isAvailable) {
            availableMoves.add(Move.DOWN);
        }

        if (y - 1 >= 0 && field.get(x).get(y - 1).isAvailable) {
            availableMoves.add(Move.LEFT);
        }

        if (y + 1 < field.get(x).size() && field.get(x).get(y + 1).isAvailable) {
            availableMoves.add(Move.RIGHT);
        }

        Random random = new Random();

        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
}
