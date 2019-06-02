package ru.roguelike.models.objects.map;

import org.junit.Test;
import ru.roguelike.models.Position;

import static org.junit.Assert.*;

public class WallTest {
    @Test
    public void test() {
        Wall w = new Wall(new Position(5, 5));
        assertFalse(w.available());
        assertEquals(new Character('#'), w.getDrawingFigure());
        assertEquals(w.getPosition(), new Position(5, 5));
    }
}