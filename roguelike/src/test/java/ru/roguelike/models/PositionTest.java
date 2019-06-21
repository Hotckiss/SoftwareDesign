package ru.roguelike.models;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class for testing position class
 */
public class PositionTest {

    /**
     * Testing getter of x coordinate
     */
    @Test
    public void getX() {
        Position p = new Position(5, 6);
        assertEquals(5, p.getX());
    }

    /**
     * Testing getter of y coordinate
     */
    @Test
    public void getY() {
        Position p = new Position(5, 6);
        assertEquals(6, p.getY());
    }

    /**
     * Testing setter of x coordinate
     */
    @Test
    public void setX() {
        Position p = new Position(5, 5);
        p.setX(10);
        assertEquals(10, p.getX());
    }

    /**
     * Testing setter of y coordinate
     */
    @Test
    public void setY() {
        Position p = new Position(5, 5);
        p.setY(10);
        assertEquals(10, p.getY());
    }

    /**
     * Testing getter of upper position
     */
    @Test
    public void up() {
        Position p = new Position(5, 5);
        assertEquals(p.up(), new Position(4, 5));
    }

    /**
     * Testing getter of left position
     */
    @Test
    public void left() {
        Position p = new Position(5, 5);
        assertEquals(p.left(), new Position(5, 4));
    }

    /**
     * Testing getter of bottom position
     */
    @Test
    public void bottom() {
        Position p = new Position(5, 5);
        assertEquals(p.bottom(), new Position(6, 5));
    }

    /**
     * Testing getter of right position
     */
    @Test
    public void right() {
        Position p = new Position(5, 5);
        assertEquals(p.right(), new Position(5, 6));
    }

    /**
     * Testing getter of current position
     */
    @Test
    public void none() {
        Position p = new Position(5, 5);
        assertEquals(p.none(), new Position(5, 5));
    }

    /**
     * Testing comparison of positions
     */
    @Test
    public void equals() {
        Position p1 = new Position(5, 5);
        Position p2 = new Position(5, 5);
        Position p3 = new Position(6, 5);
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p2, p3);
    }
}