package ru.roguelike.logic.strategies.implementations;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.strategies.Strategy;
import ru.roguelike.models.Position;

/**
 * Passive strategy: not moving, todo: attack player in next position
 */
public class PassiveStrategy implements Strategy {
    @Override
    public Move preferredMove(Position position, GameModel model) {
        return Move.NONE;
    }
}
