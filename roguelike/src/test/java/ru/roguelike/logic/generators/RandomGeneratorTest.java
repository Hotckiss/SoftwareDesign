package ru.roguelike.logic.generators;

import org.junit.Test;
import ru.roguelike.logic.GameModel;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.view.Drawable;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test class for test correctness of creating game model randomly
 */
public class RandomGeneratorTest {
    private Set<Character> mobTypes = new HashSet<>(Arrays.asList('B', 'F', 'M', 'S', 'V', 'Y'));
    private Set<Character> artifactTypes = new HashSet<>(Arrays.asList('c', 'g', 'f', 'h', 'r', 's'));

    /**
     * Testing map generation
     */
    @Test
    public void testCorrectness() {
        RandomGenerator randomGenerator = new
                RandomGenerator(10, 10, 0.1, 5, 7);

        GameModel model = randomGenerator.generate();

        int numMobs = 0, numArtifacts = 0, numPlayers = 0, numKeys = 0;

        for (List<AbstractGameObject> drawableList : model.getField()) {
            for (Drawable drawable : drawableList) {
                Character c = drawable.getDrawingFigure();

                if (c.equals('P')) {
                    numPlayers++;
                } else if (c.equals('k')) {
                    numKeys++;
                } else if (mobTypes.contains(c)) {
                    numMobs++;
                } else if (artifactTypes.contains(c)) {
                    numArtifacts++;
                } else {
                    assertTrue(c.equals('.') || c.equals('#'));
                }

            }
        }

        assertEquals(1, numPlayers);
        assertEquals(1, numKeys);
        assertEquals(5, numMobs);
        assertEquals(7, numArtifacts);
    }
}