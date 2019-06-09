package ru.roguelike.logic.generators;

import org.junit.Test;
import ru.roguelike.logic.GameModel;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.view.Drawable;
import ru.roguelike.Utils;

import java.io.*;
import java.util.List;

import static org.junit.Assert.*;

public class FromFileGeneratorTest {
    @Test
    public void testLoadedMaps() throws IOException {
        String[] fileNames = {"src/maps/map1.txt", "src/maps/map2.txt"};

        for (String fileName : fileNames) {

            FromFileGenerator generator = new FromFileGenerator(fileName);
            GameModel model = generator.generate();

            StringBuilder actualField = new StringBuilder();
            for (List<AbstractGameObject> drawableList : model.getField()) {
                for (Drawable drawable : drawableList) {
                    actualField.append(drawable.getDrawingFigure());
                }

                actualField.append("\n");
            }

            String expectedField = Utils.readFileAsString(fileName);

            assertEquals(expectedField, actualField.toString());
        }
    }
}