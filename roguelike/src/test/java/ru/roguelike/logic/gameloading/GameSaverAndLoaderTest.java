package ru.roguelike.logic.gameloading;

import org.junit.Test;
import ru.roguelike.Utils;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.generators.FromFileGenerator;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.view.Drawable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class for testing game load and save
 */
public class GameSaverAndLoaderTest {
    /**
     * Testing game save/load
     * @throws IOException
     */
    @Test
    public void test() throws IOException, ClassNotFoundException {
        String loadGameFilename = "src/maps/map1.txt";
        String savedGameFilename = "test_saving_game.txt";

        File savedGameFile = new File(savedGameFilename);
        savedGameFile.createNewFile();

        FromFileGenerator generator = new FromFileGenerator(loadGameFilename);
        GameModel gameModel = generator.generate();

        GameSaverAndLoader saverAndLoader = new GameSaverAndLoader(savedGameFilename);
        saverAndLoader.saveGame(gameModel);

        GameModel loaded = saverAndLoader.loadGame();
        assertEquals(gameModel.getKey().getPosition(), loaded.getKey().getPosition());
        assertEquals(gameModel.getActivePlayerId(), loaded.getActivePlayerId());
        assertEquals(gameModel.getMobs().size(), loaded.getMobs().size());
        assertEquals(gameModel.getArtifacts().size(), loaded.getArtifacts().size());
        assertEquals(gameModel.getLog(gameModel.getActivePlayerId()), loaded.getLog(loaded.getActivePlayerId()));

        savedGameFile.delete();
    }

}