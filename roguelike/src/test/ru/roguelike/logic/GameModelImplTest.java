package ru.roguelike.logic;

import com.googlecode.lanterna.input.KeyStroke;
import org.junit.Test;
import ru.roguelike.logic.generators.FromFileGenerator;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.view.Drawable;
import ru.roguelike.view.UserInputProvider;
import ru.roguelike.view.UserInputProviderImpl;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class GameModelImplTest {

    @Test
    public void testMakeActionSimple() throws IOException {
        KeyStroke keyStroke = new KeyStroke('a', false, false, false);
        UserInputProvider provider = new UserInputProviderImpl(keyStroke);

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

    @Test
    public void testMakeActionTakeArtifact() throws IOException {
        KeyStroke keyStrokeRight = new KeyStroke('d', false, false, false);
        KeyStroke keyStrokeDown = new KeyStroke('s', false, false, false);

        UserInputProvider providerRight = new UserInputProviderImpl(keyStrokeRight);
        UserInputProvider providerDown = new UserInputProviderImpl(keyStrokeDown);

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