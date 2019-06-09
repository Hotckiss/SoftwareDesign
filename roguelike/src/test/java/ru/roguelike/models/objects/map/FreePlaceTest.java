package ru.roguelike.models.objects.map;

import org.junit.Test;
import ru.roguelike.models.Position;

import static org.junit.Assert.*;

/**
 * Class for test free place properties
 */
public class FreePlaceTest {
    /**
     * Testing availability of free place
     */
    @Test
    public void test() {
        FreePlace fp = new FreePlace(new Position(5, 5));
        assertTrue(fp.available());
        assertEquals(new Character('.'), fp.getDrawingFigure());
        assertEquals(fp.getPosition(), new Position(5, 5));
    }
}