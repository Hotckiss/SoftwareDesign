package ru.roguelike.logic.generators;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.strategies.implementations.AggressiveStrategy;
import ru.roguelike.logic.strategies.implementations.CowardStrategy;
import ru.roguelike.logic.strategies.implementations.PassiveStrategy;
import ru.roguelike.logic.strategies.implementations.RandomStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.Mob;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Common utils for objects generation
 */
public class GenerationUtils {
    /**
     * Method thar returns free places connected to key
     * @param field game field
     * @param key final key
     * @return list of available positions
     */
    public static List<Position> connectedPositionsToKey(List<List<AbstractGameObject>> field, FinalKey key) {
        int h = field.size();
        if (h <= 0) {
            return new ArrayList<>();
        }

        int w = field.get(0).size();

        boolean[][] visited = new boolean[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                visited[i][j] = false;
            }
        }

        List<Position> result = new ArrayList<>();

        ArrayDeque<Position> bfsQueue = new ArrayDeque<>();
        bfsQueue.addLast(key.getPosition());

        while (!bfsQueue.isEmpty()) {
            Position current = bfsQueue.pollFirst();
            if (current == null) {
                break;
            }

            if (visited[current.getX()][current.getY()]) {
                continue;
            }

            if (!current.equals(key.getPosition())) {
                result.add(current);
            }

            visited[current.getX()][current.getY()] = true;
            Position left = current.left();
            Position right = current.right();
            Position bottom = current.bottom();
            Position up = current.up();

            if (left.getY() >= 0 && !(field.get(left.getX()).get(left.getY()) instanceof Wall) && !visited[left.getX()][left.getY()]) {
                bfsQueue.addLast(left);
            }

            if (right.getY() < w && !(field.get(right.getX()).get(right.getY()) instanceof Wall) && !visited[right.getX()][right.getY()]) {
                bfsQueue.addLast(right);
            }

            if (bottom.getX() < h && !(field.get(bottom.getX()).get(bottom.getY()) instanceof Wall) && !visited[bottom.getX()][bottom.getY()]) {
                bfsQueue.addLast(bottom);
            }


            if (up.getX() >= 0 && !(field.get(up.getX()).get(up.getY()) instanceof Wall) && !visited[up.getX()][up.getY()]) {
                bfsQueue.addLast(up);
            }
        }

        // only Free Places
        List<Position> filteredResult = new ArrayList<>();

        for (Position position: result) {
            if (field.get(position.getX()).get(position.getY()) instanceof FreePlace) {
                filteredResult.add(position);
            }
        }

        return filteredResult;
    }

    public static int L1(AbstractGameObject o1, AbstractGameObject o2) {
        return Math.abs(o1.getPosition().getX() - o2.getPosition().getX()) +
                Math.abs(o1.getPosition().getY() - o2.getPosition().getY());
    }

    /**
     * Generates artifact by id
     * @param alias artifact id
     * @param position artifact position
     * @return artifact
     */
    @NotNull
    public static Artifact makeArtifact(@NotNull Character alias, Position position) {
        switch (alias) {
            case 'c':
                return new Artifact(position,
                        true,
                        0,
                        0,
                        0,
                        0.2,
                        0,
                        0,
                        alias);
            case 'g':
                return new Artifact(position,
                        true,
                        0,
                        0,
                        0,
                        0,
                        0,
                        1.0,
                        alias);
            case 'f':
                return new Artifact(position,
                        true,
                        0,
                        0,
                        0.25,
                        0,
                        0,
                        0,
                        alias);
            case 'h':
                return new Artifact(position,
                        true,
                        50,
                        0,
                        0,
                        0,
                        0,
                        0,
                        alias);
            case 'r':
                return new Artifact(position,
                        true,
                        30,
                        5,
                        0,
                        0,
                        0,
                        0,
                        alias);
            case 's':
                return new Artifact(position,
                        true,
                        30,
                        5,
                        0,
                        0,
                        0,
                        0,
                        alias);
            default:
                return new Artifact(position,
                        true,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        alias);
        }
    }

    /**
     * Generates mob by id
     * @param alias mob id
     * @param position mob position
     * @return mob
     */
    @NotNull
    public static AbstractGameParticipant makeMob(@NotNull Character alias, Position position) {
        switch (alias) {
            case 'B':
                return new Mob(position,
                        30,
                        40,
                        0,
                        0,
                        0,
                        1,
                        1,
                        0,
                        4,
                        30,
                        new AggressiveStrategy(),
                        alias);
            case 'F':
                return new Mob(position,
                        10,
                        20,
                        0,
                        0,
                        0,
                        1,
                        1,
                        0,
                        2,
                        7,
                        new CowardStrategy(),
                        alias);
            case 'M' :
                return new Mob(position,
                        20,
                        40,
                        5,
                        0,
                        1,
                        1,
                        1,
                        15,
                        6,
                        30,
                        new AggressiveStrategy(),
                        alias);
            case 'S':
                return new Mob(position,
                        50,
                        20,
                        0,
                        0,
                        0,
                        1,
                        1,
                        0,
                        3,
                        18,
                        new RandomStrategy(),
                        alias);
            case 'V':
                return new Mob(position,
                        40,
                        10,
                        0,
                        0,
                        0,
                        1,
                        1,
                        10,
                        1,
                        10,
                        new PassiveStrategy(),
                        alias);
            case 'Y':
                return new Mob(position,
                        50,
                        15,
                        0,
                        0.4,
                        0,
                        1,
                        1,
                        10,
                        6,
                        25,
                        new AggressiveStrategy(),
                        alias);
            default:
                return new Mob(position,
                        50,
                        20,
                        0,
                        0,
                        0,
                        1,
                        1,
                        0,
                        3,
                        18,
                        new RandomStrategy(),
                        alias);
        }
    }
}
