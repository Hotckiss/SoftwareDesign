package ru.roguelike.logic.gameloading;

import ru.roguelike.RoguelikeLogger;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.GameModelImpl;
import ru.roguelike.logic.generators.GenerationUtils;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.Mob;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.Drawable;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that loads and saves game
 */
public class GameSaverAndLoader {
    private final static String DEFAULT_SAVED_GAME_FILENAME = "game_checkpoint.txt";
    private String savedGameFile;

    /**
     * Constructs new game loader with default path
     */
    public GameSaverAndLoader() {
        savedGameFile = DEFAULT_SAVED_GAME_FILENAME;
    }

    /**
     * Constructs new game loader with specific path
     */
    public GameSaverAndLoader(String savedGameFile) {
        this.savedGameFile = savedGameFile;
    }

    /**
     * Method that saves game model to file
     * @param game game to save
     * @throws FileNotFoundException if specified file does not exist
     */
    public void saveGame(GameModel game) throws FileNotFoundException {
        clearFile(savedGameFile);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        FileOutputStream fos = new FileOutputStream(savedGameFile);

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(game);
            out.flush();
            fos.write(bos.toByteArray());
            fos.flush();
        } catch (IOException e) {
            RoguelikeLogger.INSTANCE.log_error(e.getMessage());
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                RoguelikeLogger.INSTANCE.log_info(ex.getMessage());
            }
        }
    }

    /**
     * Method that loads game model from file
     * @return  loaded game
     */
    public GameModel loadGame() throws IOException, ClassNotFoundException {
        File file = new File(savedGameFile);
        if (!file.exists()) {
            return null;
        }

        byte[] fileContent = Files.readAllBytes(file.toPath());
        ByteArrayInputStream bis = new ByteArrayInputStream(fileContent);
        ObjectInput in = new ObjectInputStream(bis);
        Object o = in.readObject();
        return (GameModel)o;
    }

    /**
     * Method that deletes saved game
     */
    public static void deleteGame() {
        File file = new File(DEFAULT_SAVED_GAME_FILENAME);
        file.delete();
    }

    private static void clearFile(String path) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(path);
        writer.print("");
        writer.close();
    }
    
}
