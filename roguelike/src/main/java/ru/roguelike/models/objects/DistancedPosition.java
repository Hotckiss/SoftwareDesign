package ru.roguelike.models.objects;

import ru.roguelike.models.Position;

/**
 * Class that stores position and distance to some point
 */
public class DistancedPosition extends Position {
    private int distance;

    /**
     * Constructs new position
     * @param x coordinate X
     * @param y coordinate Y
     * @param distance dist to some point
     */
    public DistancedPosition(int x, int y, int distance) {
        super(x, y);
        this.distance = distance;
    }

    /**
     * Constructs new position
     * @param position point
     * @param distance dist to some point
     */
    public DistancedPosition(Position position, int distance) {
        this(position.getX(), position.getY(), distance);
    }

    /**
     * Getter for distance field
     * @return distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Getter for distance field
     * @param distance new distance to point
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }
}
