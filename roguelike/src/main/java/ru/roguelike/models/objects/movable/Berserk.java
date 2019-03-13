package ru.roguelike.models.objects.movable;

import com.googlecode.lanterna.input.KeyStroke;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.implementations.AggressiveStrategy;
import ru.roguelike.logic.strategies.implementations.RandomStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;

import java.io.IOException;

/**
 * Mob, who has higher physical damage but less health.
 */
public class Berserk extends AbstractGameParticipant {
    public Berserk(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 30;
        this.health = 30;
        this.physicalDamage = 40;
        this.fireDamage = 0;
        this.freezeProbability = 0;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 0;
        this.freezeCount = 0;
    }

    @Override
    public Character getDrawingFigure() {
        return 'B';
    }

    @Override
    public Move move(KeyStroke keyStroke, GameModel model) throws IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        return new AggressiveStrategy().preferredMove(position, model);
    }
}