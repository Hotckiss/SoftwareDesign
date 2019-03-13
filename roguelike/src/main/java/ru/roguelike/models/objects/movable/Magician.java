package ru.roguelike.models.objects.movable;

import com.googlecode.lanterna.input.KeyStroke;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.implementations.AggressiveStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;

import java.io.IOException;

/**
 * Has less health but can regenerate quickly.
 * Has small physical damage, but can strike only with fire.
 */
public class Magician extends AbstractGameParticipant {
    public Magician(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 20;
        this.health = 20;
        this.physicalDamage = 40;
        this.fireDamage = 10;
        this.freezeProbability = 0;
        this.fireProbability = 1;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 15;
        this.freezeCount = 0;
    }

    @Override
    public Character getDrawingFigure() {
        return 'M';
    }

    @Override
    public Move move(KeyStroke keyStroke, GameModel model) throws IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        return new AggressiveStrategy().preferredMove(position, model);
    }
}