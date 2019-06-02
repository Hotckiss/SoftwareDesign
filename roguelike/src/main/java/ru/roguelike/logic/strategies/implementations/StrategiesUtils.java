package ru.roguelike.logic.strategies.implementations;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.models.objects.base.Artifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.Player;

import java.util.List;

/**
 * Utils for available positions
 */
public class StrategiesUtils {
    /**
     * Checks that mob can visit position
     * @param field game field
     * @param x x coordinate of position
     * @param y y coordinate of position
     * @return true if position is available false otherwise
     */
    public static boolean availableForMob(@NotNull List<List<AbstractGameObject>> field,
                                    int x, int y) {
        return !(field.get(x).get(y) instanceof Wall ||
                field.get(x).get(y) instanceof Artifact);
    }

    /**
     * Checks that coward mob can visit position(can't touch player)
     * @param field game field
     * @param x x coordinate of position
     * @param y y coordinate of position
     * @return true if position is available false otherwise
     */
    public static boolean availableForCowardMob(@NotNull List<List<AbstractGameObject>> field,
                                          int x, int y) {
        return !(field.get(x).get(y) instanceof Wall ||
                field.get(x).get(y) instanceof Artifact ||
                field.get(x).get(y) instanceof Player);
    }
}
