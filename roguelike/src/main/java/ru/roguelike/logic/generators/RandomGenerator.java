package ru.roguelike.logic.generators;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.GameModelImpl;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.*;
import ru.roguelike.models.objects.base.AbstractArtifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGenerator implements GameGenerator {
    private static String[] mobsIds = { "ber", "simple", "flier", "mag", "vam", "yeti" };
    private static String[] artifactsIds = { "csword", "fsword", "fgoblet", "hw", "rp", "sb" };

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

    @Override
    public GameModel generate() {
        List<List<AbstractGameObject>> field = new ArrayList<>();
        Player player;
        FinalKey key;
        List<AbstractGameParticipant> mobs = new ArrayList<>();
        List<AbstractArtifact> artifacts = new ArrayList<>();

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
            String id = mobsIds[random.nextInt(mobsIds.length)];
            AbstractGameParticipant mob = makeMobForId(id, position);
            mobs.add(mob);
            field.get(position.getX()).set(position.getY(), mob);
            totalMobs++;
        }

        while (totalArtifacts < artifactsCount) {
            Position position = generateRandomPosition(random);
            String id = artifactsIds[random.nextInt(artifactsIds.length)];
            AbstractArtifact artifact = makeArtifactForId(id, position);
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

    private AbstractArtifact makeArtifactForId(String id, Position position) {
        switch (id) {
            case "csword":
                return new ColdSword(position);
            case "fsword":
                return new FireSword(position);
            case "fgoblet":
                return new FireGoblet(position);
            case "hw":
                return new HealthWater(position);
            case "rp":
                return new RegenerationPotion(position);
            case "sb":
                return new SpeedBoots(position);
            default:
                return new HealthWater(position);
        }
    }

    private AbstractGameParticipant makeMobForId(String id, Position position) {
        switch (id) {
            case "ber":
                return new Berserk(position);
            case "simple":
                return new SimpleMob(position);
            case "flier":
                return new Flier(position);
            case "mag":
                return new Magician(position);
            case "vam":
                return new Vampire(position);
            case "yeti":
                return new Yeti(position);
            default:
                return new SimpleMob(position);
        }
    }

    private Position generateRandomPosition(Random random) {
        int x = random.nextInt(width);
        int y = random.nextInt(height);

        return new Position(x, y);
    }
}
