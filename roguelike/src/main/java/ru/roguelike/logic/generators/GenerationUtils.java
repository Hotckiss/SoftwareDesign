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
     * @param position position to start search
     * @return list of available positions
     */
    public static List<Position> connectedPositions(List<List<AbstractGameObject>> field, Position position) {
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
        bfsQueue.addLast(position);

        while (!bfsQueue.isEmpty()) {
            Position current = bfsQueue.pollFirst();
            if (current == null) {
                break;
            }

            if (visited[current.getX()][current.getY()]) {
                continue;
            }

            if (!current.equals(position)) {
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

        for (Position pos: result) {
            if (field.get(pos.getX()).get(pos.getY()) instanceof FreePlace) {
                filteredResult.add(pos);
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
        Artifact.ArtifactBuilder builder =
                new Artifact.ArtifactBuilder(position,true, alias);
        switch (alias) {
            case 'c':
                return builder.freezeProbabilityBonus(0.2).build();
            case 'g':
                return builder.fireDamageMultiplierBonus(1.0).build();
            case 'f':
                return builder.fireProbabilityBonus(0.25).build();
            case 'h':
                return builder.restoringHealth(50).build();
            case 'r':
                return builder.restoringHealth(30).regenerationBonus(5).build();
            case 's':
                return builder.regenerationBonus(10).build();
            default:
                return builder.build();
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
        Mob.MobBuilder builder = new Mob.MobBuilder(position, alias);
        switch (alias) {
            case 'B':
                return builder
                        .fullHealth(30)
                        .physicalDamage(40)
                        .experienceCostHit(4)
                        .experienceCostKill(30)
                        .defaultStrategy(new AggressiveStrategy())
                        .build();
            case 'F':
                return builder
                        .fullHealth(10)
                        .physicalDamage(20)
                        .experienceCostHit(2)
                        .experienceCostKill(7)
                        .defaultStrategy(new CowardStrategy())
                        .build();
            case 'M' :
                return builder
                        .fullHealth(20)
                        .physicalDamage(40)
                        .fireDamage(5)
                        .fireProbability(1)
                        .regeneration(15)
                        .experienceCostHit(6)
                        .experienceCostKill(30)
                        .defaultStrategy(new AggressiveStrategy())
                        .build();
            case 'S':
                return builder
                        .fullHealth(50)
                        .physicalDamage(20)
                        .experienceCostHit(3)
                        .experienceCostKill(18)
                        .defaultStrategy(new RandomStrategy())
                        .build();
            case 'V':
                return builder
                        .fullHealth(40)
                        .physicalDamage(10)
                        .experienceCostHit(1)
                        .experienceCostKill(10)
                        .regeneration(10)
                        .defaultStrategy(new PassiveStrategy())
                        .build();
            case 'Y':
                return builder
                        .fullHealth(50)
                        .physicalDamage(15)
                        .freezeProbability(0.4)
                        .regeneration(10)
                        .experienceCostHit(6)
                        .experienceCostKill(25)
                        .defaultStrategy(new AggressiveStrategy())
                        .build();
            default:
                return builder
                        .fullHealth(50)
                        .physicalDamage(20)
                        .experienceCostHit(3)
                        .experienceCostKill(18)
                        .defaultStrategy(new RandomStrategy())
                        .build();
        }
    }
}
