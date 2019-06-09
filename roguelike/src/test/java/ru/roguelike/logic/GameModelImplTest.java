package ru.roguelike.logic;

import org.junit.Test;
import ru.roguelike.logic.generators.FromFileGenerator;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.view.Drawable;
import ru.roguelike.view.StringStreamInputProviderImpl;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Testing game model
 */
public class GameModelImplTest {

    /**
     * Testing move step
     * @throws IOException
     */
    @Test
    public void testMakeActionSimple() throws IOException {
        UserInputProvider provider = new StringStreamInputProviderImpl("a");

        String fileName = "src/maps/map1.txt";
        int fieldWidth = 9;

        FromFileGenerator generator = new FromFileGenerator(fileName);
        GameModel model = generator.generate();

        model.makeMove(provider);

        StringBuilder actualField = new StringBuilder();
        for (List<AbstractGameObject> drawableList : model.getField()) {
            for (Drawable drawable : drawableList) {
                actualField.append(drawable.getDrawingFigure());
            }

            actualField.append("\n");
        }

        assertEquals('P', actualField.toString().charAt(2 * (fieldWidth + 1) + 2));
    }

    /**
     * Testing getting artifact step
     * @throws IOException
     */
    @Test
    public void testMakeActionTakeArtifact() throws IOException {
        UserInputProvider providerRight = new StringStreamInputProviderImpl("d");
        UserInputProvider providerDown = new StringStreamInputProviderImpl("s");

        String fileName = "src/maps/map2.txt";
        int fieldWidth = 14;

        FromFileGenerator generator = new FromFileGenerator(fileName);
        GameModel model = generator.generate();

        model.makeMove(providerRight);
        model.makeMove(providerDown);

        StringBuilder actualField = new StringBuilder();
        for (List<AbstractGameObject> drawableList : model.getField()) {
            for (Drawable drawable : drawableList) {
                actualField.append(drawable.getDrawingFigure());
            }

            actualField.append("\n");
        }

        assertEquals('P', actualField.toString().charAt((fieldWidth + 1) + 2));
    }
}