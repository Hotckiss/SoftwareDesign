package ru.roguelike.logic.generators;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.GameModelImpl;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.*;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates a random field for a new game.
 */
public class RandomGenerator implements GameGenerator {
    private static Character[] mobsIds = {'B', 'F', 'M', 'S', 'V', 'Y'};
    private static Character[] artifactsIds = {'c', 'g', 'f', 'h', 'r', 's'};

    private double wallsProbability;
    private int mobsCount;
    private int artifactsCount;
    private int width;
    private int height;

    /**
     * Constructs new random map generator
     * @param width field width
     * @param height field height
     * @param wallsProbability walls density
     * @param mobsCount mobs count
     * @param artifactsCount artifacts count
     */
    public RandomGenerator(int width, int height, double wallsProbability, int mobsCount, int artifactsCount) {
        this.wallsProbability = wallsProbability;
        this.mobsCount = mobsCount;
        this.artifactsCount = artifactsCount;
        this.width = width;
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameModel generate() {
        List<List<AbstractGameObject>> field = new ArrayList<>();
        Player player;
        FinalKey key;
        List<AbstractGameParticipant> mobs = new ArrayList<>();
        List<Artifact> artifacts = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < width; i++) {
            field.add(new ArrayList<>());
            for (int j = 0; j < height; j++) {
                if (random.nextDouble() <= wallsProbability) {
                    field.get(i).add(new Wall(new Position(i, j)));
                } else {
                    field.get(i).add(new FreePlace(new Position(i, j)));
                }
            }
        }

        int totalMobs = 0;
        int totalArtifacts = 0;

        while (totalMobs < mobsCount) {
            Position position = generateRandomPosition(random);

            AbstractGameObject curObject = field.get(position.getX()).get(position.getY());
            if (!(curObject instanceof FreePlace)) {
                continue;
            }

            Character alias = mobsIds[random.nextInt(mobsIds.length)];
            AbstractGameParticipant mob = GenerationUtils.makeMob(alias, position);
            mobs.add(mob);
            field.get(position.getX()).set(position.getY(), mob);
            totalMobs++;
        }

        while (totalArtifacts < artifactsCount) {
            Position position = generateRandomPosition(random);

            AbstractGameObject curObject = field.get(position.getX()).get(position.getY());
            if (!(curObject instanceof FreePlace)) {
                continue;
            }

            Character alias = artifactsIds[random.nextInt(artifactsIds.length)];
            Artifact artifact = GenerationUtils.makeArtifact(alias, position);
            artifacts.add(artifact);
            field.get(position.getX()).set(position.getY(), artifact);
            totalArtifacts++;
        }

        Position positionKey = generateRandomPosition(random);
        while (!(field.get(positionKey.getX()).get(positionKey.getY()) instanceof FreePlace)) {
            positionKey = generateRandomPosition(random);
        }

        key = new FinalKey(positionKey);
        field.get(positionKey.getX()).set(positionKey.getY(), key);

        List<Position> availableForPlayer = GenerationUtils.connectedPositionsToKey(field, key);

        // put player next to key if no positions
        if (availableForPlayer.isEmpty()) {
            Position keyPosition = key.getPosition();
            Position left = keyPosition.left();
            Position right = keyPosition.right();
            Position bottom = keyPosition.bottom();
            Position up = keyPosition.up();

            Position positionPlayer = generateRandomPosition(random);

            if (left.getY() >= 0) {
                positionPlayer = left;
            }

            if (right.getY() < field.size()) {
                positionPlayer = right;
            }

            if (bottom.getX() < field.get(0).size()) {
                positionPlayer = bottom;
            }

            if (up.getX() >= 0 ) {
                positionPlayer = up;
            }

            player = new Player(positionPlayer);
            field.get(positionPlayer.getX()).set(positionPlayer.getY(), player);

            // remove if player stands on artifact or mob
            removeBadItems(artifacts, positionPlayer);
            removeBadItems(mobs, positionPlayer);

            return new GameModelImpl(field, player, key, mobs, artifacts);
        }

        Position positionPlayer = availableForPlayer.get(random.nextInt(availableForPlayer.size()));

        player = new Player(positionPlayer);
        field.get(positionPlayer.getX()).set(positionPlayer.getY(), player);

        return new GameModelImpl(field, player, key, mobs, artifacts);
    }

    /**
     * Removes artifact or mob that conflicts to player or key position
     * @param items artifacts or mobs
     * @param freePosition position to clear
     */
    private <T extends AbstractGameObject> void removeBadItems(List<T> items, Position freePosition) {
        for (AbstractGameObject item: items) {
            if (freePosition.equals(item.getPosition())) {
                items.remove(item);
                return;
            }
        }
    }

    /**
     * Generates random position on map
     * @param random random generator
     * @return generated position
     */
    @NotNull
    private Position generateRandomPosition(@NotNull Random random) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);

        return new Position(x, y);
    }
}