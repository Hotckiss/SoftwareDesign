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
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.ConsoleViewImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FromFileGenerator implements GameGenerator {
    private boolean ifFileExist = true;
    private List<String> mapFromFile = new ArrayList<>();

    public FromFileGenerator(String fileName) {
        System.out.println(fileName);

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

    @Override
    public GameModel generate() {
        if (!ifFileExist) {
            System.out.println("NOT FILE");
            return null;
        }

        List<List<AbstractGameObject>> field = new ArrayList<>();
        Player player = null;
        FinalKey key = null;
        List<AbstractGameParticipant> mobs = new ArrayList<>();
        List<AbstractArtifact> artifacts = new ArrayList<>();

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
                        field.get(i).add(getMobFromChar(c, new Position(i, j)));
                        break;
                    case 'c':
                    case 'g':
                    case 'f':
                    case 'h':
                    case 'r':
                    case 's':
                        field.get(i).add(getArtifactFromChar(c, new Position(i, j)));
                        break;
                    default:
                        return null;
                }
            }
        }

        return new GameModelImpl(field, player, key, mobs, artifacts);
    }

    private AbstractArtifact getArtifactFromChar(char c, Position position) {
        switch (c) {
            case 'c':
                return new ColdSword(position);
            case 'g':
                return new FireGoblet(position);
            case 'f':
                return new FireSword(position);
            case 'h':
                return new HealthWater(position);
            case 'r':
                return new RegenerationPotion(position);
            case 's':
                return new SpeedBoots(position);
        }

        return null;
    }

    private AbstractGameParticipant getMobFromChar(char c, Position position) {
        switch (c) {
            case 'B':
                return new Berserk(position);
            case 'F':
                return new Flier(position);
            case 'M' :
                return new Magician(position);
            case 'S':
                return new SimpleMob(position);
            case 'V':
                return new Vampire(position);
            case 'Y':
                return new Yeti(position);
        }

        return null;
    }

    public static void main(String[] args) throws IOException {
        FromFileGenerator generator = new FromFileGenerator("/Users/aliscafo/map.txt");

        GameModel model = generator.generate();

        System.out.println(model.makeDrawable());

        ConsoleView consoleView = new ConsoleViewImpl();
        consoleView.draw(model.makeDrawable(), model.getInfo(), model.getLog(),
                model.isShowHelpScreen(), model.isLoadMapFromFile());

    }
}
