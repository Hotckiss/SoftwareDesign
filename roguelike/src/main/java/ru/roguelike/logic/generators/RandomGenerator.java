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

            if (!(curObject.getClass().equals(FreePlace.class) ||
                    curObject.getClass().equals(Wall.class))) {
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

            if (!(curObject.getClass().equals(FreePlace.class) ||
                    curObject.getClass().equals(Wall.class))) {
                continue;
            }

            Character alias = artifactsIds[random.nextInt(artifactsIds.length)];
            Artifact artifact = GenerationUtils.makeArtifact(alias, position);
            artifacts.add(artifact);
            field.get(position.getX()).set(position.getY(), artifact);
            totalArtifacts++;
        }

        Position position = generateRandomPosition(random);
        player = new Player(position);
        field.get(position.getX()).set(position.getY(), player);

        position = generateRandomPosition(random);
        key = new FinalKey(position);
        field.get(position.getX()).set(position.getY(), key);

        return new GameModelImpl(field, player, key, mobs, artifacts);
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