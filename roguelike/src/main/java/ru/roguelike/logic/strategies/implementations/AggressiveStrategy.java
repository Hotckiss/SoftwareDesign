package ru.roguelike.logic.strategies.implementations;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.logic.generators.GenerationUtils;
import ru.roguelike.logic.strategies.AbstractStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.DistancedPosition;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.Mob;
import ru.roguelike.models.objects.movable.Player;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

/**
 * Aggressive strategy: attack player in next position
 */
public class AggressiveStrategy extends AbstractStrategy implements Serializable {
    private static final int OVERVIEW_RADIUS = 5;
    private AbstractStrategy defaultStrategy = new RandomStrategy();
    /**
     * {@inheritDoc}
     */
    @Override
    public Move preferredMove(Mob mob, GameModel model) {
        List<List<AbstractGameObject>> field = model.getField();
        Position mobPosition = mob.getPosition();
        Predicate<AbstractGameObject> mobAvailableTest = abstractGameObject -> {
            Position position = abstractGameObject.getPosition();
            AbstractGameObject obj = field.get(position.getX()).get(position.getY());
            return !(obj instanceof Wall || obj instanceof Artifact || obj instanceof Mob) || position.equals(mobPosition);
        };
        int minDistance = Integer.MAX_VALUE;
        Move bestMove = Move.NONE;

        for (Player player: model.getAllPlayers()) {
            Position playerPosition = player.getPosition();
            List<DistancedPosition> connectedToPlayer =
                    GenerationUtils.connectedPositions(field, playerPosition, mobAvailableTest, true, false);


            int leftIndex = connectedToPlayer.indexOf(mobPosition.left());
            int rightIndex = connectedToPlayer.indexOf(mobPosition.right());
            int bottomIndex = connectedToPlayer.indexOf(mobPosition.bottom());
            int upIndex = connectedToPlayer.indexOf(mobPosition.up());

            if (upIndex >= 0 && connectedToPlayer.get(upIndex).getDistance() < minDistance) {
                minDistance = connectedToPlayer.get(upIndex).getDistance();
                bestMove = Move.UP;
            }

            if (bottomIndex >= 0 && connectedToPlayer.get(bottomIndex).getDistance() < minDistance) {
                minDistance = connectedToPlayer.get(bottomIndex).getDistance();
                bestMove = Move.DOWN;
            }

            if (rightIndex >= 0 && connectedToPlayer.get(rightIndex).getDistance() < minDistance) {
                minDistance = connectedToPlayer.get(rightIndex).getDistance();
                bestMove = Move.RIGHT;
            }

            if (leftIndex >= 0 && connectedToPlayer.get(leftIndex).getDistance() < minDistance) {
                minDistance = connectedToPlayer.get(leftIndex).getDistance();
                bestMove = Move.LEFT;
            }
        }

        if (minDistance <= OVERVIEW_RADIUS) {
            return bestMove;
        }

        return defaultStrategy.preferredMove(mob, model);
    }
}
