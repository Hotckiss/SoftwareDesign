package ru.roguelike.models;

/**
 * Represents a position of object.
 */
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public Position top() {
        return new Position(x - 1, y);
    }

    public Position left() {
        return new Position(x, y - 1);
    }

    public Position bottom() {
        return new Position(x + 1, y);
    }

    public Position right() {
        return new Position(x, y + 1);
    }

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
