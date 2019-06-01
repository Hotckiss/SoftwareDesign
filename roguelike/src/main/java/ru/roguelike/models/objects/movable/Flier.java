package ru.roguelike.models.objects.movable;

import com.googlecode.lanterna.input.KeyStroke;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.implementations.AggressiveStrategy;
import ru.roguelike.logic.strategies.implementations.CowardStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;

import java.io.IOException;

/**
 * Mob which is able to make to strokes in one move.
 * Has less help than simple mob.
 */
public class Flier extends AbstractGameParticipant {
    /**
     * Constructs new Flier on specified position
     * @param position position to add Flier
     */
    public Flier(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 10;
        this.health = 10;
        this.physicalDamage = 20;
        this.fireDamage = 0;
        this.freezeProbability = 0;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 0;
        this.freezeCount = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getDrawingFigure() {
        return 'F';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Move move(KeyStroke keyStroke, GameModel model) throws IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        return new CowardStrategy().preferredMove(position, model);
    }
}