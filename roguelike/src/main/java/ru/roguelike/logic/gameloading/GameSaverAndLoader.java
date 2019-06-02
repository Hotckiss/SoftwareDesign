package ru.roguelike.logic.gameloading;

import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.GameModelImpl;
import ru.roguelike.logic.generators.FromFileGenerator;
import ru.roguelike.logic.generators.GenerationUtils;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.base.Artifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.Drawable;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class GameSaverAndLoader {
    private String savedGameFile = "game_checkpoint.txt";

    public void saveGame(GameModel game) throws FileNotFoundException {
        clearFile(savedGameFile);

        List<List<AbstractGameObject>> fieldModel = game.getField();
        Player player = game.getPlayer();
        FinalKey key = game.getKey();
        List<AbstractGameParticipant> mobs = game.getMobs();

        // FIELD INFO
        int n = fieldModel.size();
        int m = fieldModel.get(0).size();
        appendToFile(savedGameFile, n + " " + m + "\n");

        for (List<AbstractGameObject> line : fieldModel) {
            StringBuilder builder = new StringBuilder();
            for (Drawable drawable : line) {
                builder.append(drawable.getDrawingFigure());
            }

            appendToFile(savedGameFile, builder.toString() + "\n");
        }

        // PLAYER INFO
        Position position = player.getPosition();
        appendToFile(savedGameFile, position.getX() + " " + position.getY() + "\n");
        appendToFile(savedGameFile,  player.exp() + "\n");
        appendToFile(savedGameFile, player.getArtifactsLog() + "\n");
        ArrayDeque<Player.ArtifactItem> artifactItems = player.getArtifacts();
        boolean isEquipped = false;
        if (!artifactItems.isEmpty() && artifactItems.getFirst().equipped()) {
            isEquipped = true;
        }
        appendToFile(savedGameFile, isEquipped + "\n");
        appendToFile(savedGameFile, getFeaturesAsString(player) + "\n");

        // KEY INFO
        Position keyPosition = key.getPosition();
        appendToFile(savedGameFile, keyPosition.getX() + " " + keyPosition.getY() + "\n");

        // MOBS INFO
        appendToFile(savedGameFile, mobs.size() + "\n");
        for (AbstractGameParticipant mob : mobs) {
            Position mobPosition = mob.getPosition();
            appendToFile(savedGameFile, mobPosition.getX() + " " + mobPosition.getY() + "\n");
            appendToFile(savedGameFile, getFeaturesAsString(mob) + "\n");
        }
    }

    public GameModel loadGame() {
        File file = new File(savedGameFile);
        if (!file.exists()) {
            return null;
        }

        List<List<AbstractGameObject>> field = new ArrayList<>();
        Player player = null;
        FinalKey key = null;
        List<AbstractGameParticipant> mobs = new ArrayList<>();
        List<Artifact> artifacts = new ArrayList<>();

        List<String> mapFromFile = new ArrayList<>();
        FromFileGenerator generator = new FromFileGenerator();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(savedGameFile));

            // GETTING FIELD INFO
            String line = reader.readLine();
            String[] sizes = line.split("\\s+");
            int n = Integer.parseInt(sizes[0]);
            int m = Integer.parseInt(sizes[1]);

            for (int i = 0; i < n; i++) {
                line = reader.readLine();
                List<AbstractGameObject> lineList = new ArrayList<>();
                mapFromFile.add(line);

                for (int j = 0; j < m; j++) {
                    char c = line.charAt(j);
                    switch (c) {
                        case '.':
                            lineList.add(new FreePlace(new Position(i, j)));
                            break;
                        case '#':
                            lineList.add(new Wall(new Position(i, j)));
                            break;
                        case 'c':
                        case 'g':
                        case 'f':
                        case 'h':
                        case 'r':
                        case 's':
                            Artifact artifact = GenerationUtils.makeArtifact(c, new Position(i, j));
                            artifacts.add(artifact);
                            lineList.add(artifact);
                            break;
                        default:
                            lineList.add(new FreePlace(new Position(i, j)));
                            break;

                    }
                }
                field.add(lineList);
            }

            // GETTING PLAYER INFO
            line = reader.readLine();
            String[] coords = line.split("\\s+");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            player = new Player(new Position(x, y));

            line = reader.readLine();
            int exp = Integer.parseInt(line);
            player.setExperience(exp);

            line = reader.readLine();
            ArrayDeque<Player.ArtifactItem> artifactItems = new ArrayDeque<>();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                Artifact artifact = GenerationUtils.makeArtifact(c, new Position(-1, -1));
                artifactItems.add(new Player.ArtifactItem(artifact));
            }

            line = reader.readLine();
            if (line.equals("true")) {
                artifactItems.getFirst().equip();
            }

            player.setArtifacts(artifactItems);

            line = reader.readLine();
            setFeatures(player, line);

            List<AbstractGameObject> lineList = field.get(x);
            lineList.set(y, player);
            field.set(x, lineList);

            // GETTING KEY INFO
            line = reader.readLine();
            coords = line.split("\\s+");
            x = Integer.parseInt(coords[0]);
            y = Integer.parseInt(coords[1]);
            key = new FinalKey(new Position(x, y));

            lineList = field.get(x);
            lineList.set(y, key);
            field.set(x, lineList);

            // GETTING MOBS INFO
            line = reader.readLine();
            int mobsNum = Integer.parseInt(line);

            for (int i = 0; i < mobsNum; i++) {
                line = reader.readLine();
                coords = line.split("\\s+");
                x = Integer.parseInt(coords[0]);
                y = Integer.parseInt(coords[1]);

                char c = mapFromFile.get(x).charAt(y);
                AbstractGameParticipant mob = GenerationUtils.makeMob(c, new Position(x, y));

                line = reader.readLine();
                setFeatures(mob, line);

                mobs.add(mob);

                lineList = field.get(x);
                lineList.set(y, mob);
                field.set(x, lineList);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameModel gameModel = new GameModelImpl(field, player, key, mobs, artifacts);
        gameModel.setSavedGameEqualToCurrent(true);

        return gameModel;
    }

    public void deleteGame() {
        File file = new File(savedGameFile);
        file.delete();
    }

    private String getFeaturesAsString(AbstractGameParticipant participant) {
        return participant.getFullHealth() + " " +
               participant.getHealth() + " " +
               participant.getPhysicalDamage() + " " +
               participant.getFireDamage() + " " +
               participant.getFreezeProbability() + " " +
               participant.getFireProbability() + " " +
               participant.getPhysicalDamageMultiplier() + " " +
               participant.getFireDamageMultiplier() + " " +
               participant.getRegeneration() + " " +
               participant.getFreezeCount() + " " +
               participant.getFireCount() + " " +
               participant.getFireValue();
    }

    private void setFeatures(AbstractGameParticipant participant, String featuresString) {
        String[] features = featuresString.split("\\s+");
        participant.setFullHealth(Integer.parseInt(features[0]));
        participant.setHealth(Integer.parseInt(features[1]));
        participant.setPhysicalDamage(Integer.parseInt(features[2]));
        participant.setFireDamage(Integer.parseInt(features[3]));
        participant.setFreezeProbability(Double.parseDouble(features[4]));
        participant.setFireProbability(Double.parseDouble(features[5]));
        participant.setPhysicalDamageMultiplier(Double.parseDouble(features[6]));
        participant.setFireDamageMultiplier(Double.parseDouble(features[7]));
        participant.setRegeneration(Integer.parseInt(features[8]));
        participant.setFreezeCount(Integer.parseInt(features[9]));
        participant.setFireCount(Integer.parseInt(features[10]));
        participant.setFireValue(Integer.parseInt(features[11]));
    }

    public static void appendToFile(String filePath, String text) {
        File file = new File(filePath);
        FileWriter fr = null;
        BufferedWriter br = null;
        PrintWriter pr = null;
        try {
            // to append to file, you need to initialize FileWriter using below constructor
            fr = new FileWriter(file, true);
            br = new BufferedWriter(fr);
            pr = new PrintWriter(br);
            pr.print(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                pr.close();
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void clearFile(String path) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(path);
        writer.print("");
        writer.close();
    }
    
}
