package ru.roguelike.models;

import java.io.Serializable;

/**
 * Represents a position of object.
 */
public class Position implements Serializable {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets X coordinate of position
     * @return X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets Y coordinate of position
     * @return Y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Sets X coordinate of position
     * @param newX new X coordinate
     */
    public void setX(int newX) {
        this.x = newX;
    }

    /**
     * Sets Y coordinate of position
     * @param newY new Y coordinate
     */
    public void setY(int newY) {
        this.y = newY;
    }

    /**
     * Returns position above current
     * @return above position
     */
    public Position up() {
        return new Position(x - 1, y);
    }

    /**
     * Returns position left to current
     * @return left position
     */
    public Position left() {
        return new Position(x, y - 1);
    }

    /**
     * Returns position bottom current
     * @return bottom position
     */
    public Position bottom() {
        return new Position(x + 1, y);
    }

    /**
     * Returns position right to current
     * @return right position
     */
    public Position right() {
        return new Position(x, y + 1);
    }

    /**
     * Returns position equal to current
     * @return equal position
     */
    public Position none() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Position && ((Position)obj).getX() == getX()
                && ((Position)obj).getY() == getY();
    }

    @Override
    public int hashCode() {
        //TODO
        return super.hashCode();
    }
}
