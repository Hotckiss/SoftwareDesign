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

public class GameSaverAndLoaderTest {
    @Test
    public void testSavingGame() throws IOException {
        String loadGameFilename = "src/maps/map1.txt";
        String savedGameFilename = "test_saving_game.txt";

        File savedGameFile = new File(savedGameFilename);
        savedGameFile.createNewFile();

        FromFileGenerator generator = new FromFileGenerator(loadGameFilename);
        GameModel gameModel = generator.generate();

        GameSaverAndLoader saverAndLoader = new GameSaverAndLoader(savedGameFilename);
        saverAndLoader.saveGame(gameModel);

        String actualContent = Utils.readFileAsString(savedGameFilename);
        String expectedContent = "6 9\n" +
                "...#M....\n" +
                "......#..\n" +
                ".#.P..Y..\n" +
                ".......c.\n" +
                ".###.....\n" +
                ".s..V...k\n" +
                "2 3\n" +
                "0\n" +
                "\n" +
                "false\n" +
                "100 100 20 5 0.0 0.0 1.0 1.0 5 0 0 0\n" +
                "5 8\n" +
                "3\n" +
                "0 4\n" +
                "20 20 40 5 0.0 1.0 1.0 1.0 15 0 0 0\n" +
                "2 6\n" +
                "50 50 15 0 0.4 0.0 1.0 1.0 10 0 0 0\n" +
                "5 4\n" +
                "40 40 10 0 0.0 0.0 1.0 1.0 10 0 0 0\n";

        assertEquals(expectedContent, actualContent);

        savedGameFile.delete();
    }

    @Test
    public void testLoadingGame() throws IOException {
        String loadGameFilename = "src/maps/map1.txt";
        String savedGameFilename = "test_saving_game.txt";

        File savedGameFile = new File(savedGameFilename);
        savedGameFile.createNewFile();

        FromFileGenerator generator = new FromFileGenerator(loadGameFilename);
        GameModel gameModel = generator.generate();

        GameSaverAndLoader saverAndLoader = new GameSaverAndLoader(savedGameFilename);
        saverAndLoader.saveGame(gameModel);

        GameModel loadedGameModel = saverAndLoader.loadGame();

        assertEquals(gameModel.getField().size(), loadedGameModel.getField().size());

        for (int i = 0; i < gameModel.getField().size(); i++) {
            assertEquals(gameModel.getField().get(i).size(), loadedGameModel.getField().get(i).size());

            for (int j = 0; j < gameModel.getField().get(i).size(); j++) {
                assertEquals(gameModel.getField().get(i).get(j).getDrawingFigure(),
                        loadedGameModel.getField().get(i).get(j).getDrawingFigure());
            }
        }
        
        savedGameFile.delete();
    }
}