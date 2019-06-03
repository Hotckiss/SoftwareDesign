package ru.roguelike.logic.generators;

import org.junit.Test;
import ru.roguelike.logic.GameModel;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.view.Drawable;

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

            String expectedField = readFileAsString(fileName);

            assertEquals(expectedField, actualField.toString());
        }
    }

    public static String readFileAsString(String fileName) throws IOException {
        InputStream is = new FileInputStream(fileName);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while(line != null){
            sb.append(line).append("\n");
            line = buf.readLine();
        }

        String fileAsString = sb.toString();

        is.close();
        buf.close();

        return fileAsString;
    }
}