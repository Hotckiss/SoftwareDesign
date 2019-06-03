package ru.roguelike.logic.generators;

import ru.roguelike.RoguelikeLogger;
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Map loader. Loads file from specified file
 */
public class  FromFileGenerator implements GameGenerator {
    private boolean ifFileExist = true;
    private List<String> mapFromFile = new ArrayList<>();

    /**
     * Creates new map generator with specified path
     * @param fileName path to file with map
     */
    public FromFileGenerator(String fileName) {
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                mapFromFile.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            ifFileExist = false;
        }
    }

    /**
     * Creates empty generator
     */
    public FromFileGenerator() {
        ifFileExist = false;
    }

    /**
     * Generates map from file
     * @return generated map
     */
    @Override
    public GameModel generate() {
        if (!ifFileExist) {
            RoguelikeLogger.INSTANCE.log_info("File doesn't exist");
            return null;
        }

        List<List<AbstractGameObject>> field = new ArrayList<>();
        Player player = null;
        FinalKey key = null;
        List<AbstractGameParticipant> mobs = new ArrayList<>();
        List<Artifact> artifacts = new ArrayList<>();

        int n = mapFromFile.size();
        if (n == 0) {
            return null;
        }

        int m = mapFromFile.get(0).length();
        if (m == 0) {
            return null;
        }

        for (int i = 0; i < n; i++) {
            field.add(new ArrayList<>());
            for (int j = 0; j < m; j++) {

                char c;
                try {
                    c = mapFromFile.get(i).charAt(j);
                } catch (Exception e) {
                    return null;
                }

                switch (c) {
                    case '.':
                        field.get(i).add(new FreePlace(new Position(i, j)));
                        break;
                    case '#':
                        field.get(i).add(new Wall(new Position(i, j)));
                        break;
                    case 'P':
                        if (player != null) {
                            return null;
                        }
                        player = new Player(new Position(i, j));
                        field.get(i).add(player);
                        break;
                    case 'k':
                        if (key != null) {
                            return null;
                        }
                        key = new FinalKey(new Position(i, j));
                        field.get(i).add(key);
                        break;
                    case 'B':
                    case 'F':
                    case 'M':
                    case 'S':
                    case 'V':
                    case 'Y':
                        AbstractGameParticipant mob = GenerationUtils.makeMob(c, new Position(i, j));
                        mobs.add(mob);
                        field.get(i).add(mob);
                        break;
                    case 'c':
                    case 'g':
                    case 'f':
                    case 'h':
                    case 'r':
                    case 's':
                        Artifact artifact = GenerationUtils.makeArtifact(c, new Position(i, j));
                        artifacts.add(artifact);
                        field.get(i).add(artifact);
                        break;
                    default:
                        return null;
                }
            }
        }

        return new GameModelImpl(field, player, key, mobs, artifacts);
    }
}
