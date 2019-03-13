package ru.roguelike.logic.strategies.implementations;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.models.objects.base.AbstractArtifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.Player;

import java.util.List;

/**
 * Utils for available positions
 */
public class StrategiesUtils {
    public static boolean availableForMob(@NotNull List<List<AbstractGameObject>> field,
                                    int x, int y) {
        return !(field.get(x).get(y) instanceof Wall ||
                field.get(x).get(y) instanceof AbstractArtifact);
    }

    public static boolean availableForCowardMob(@NotNull List<List<AbstractGameObject>> field,
                                          int x, int y) {
        return !(field.get(x).get(y) instanceof Wall ||
                field.get(x).get(y) instanceof AbstractArtifact ||
                field.get(x).get(y) instanceof Player);
    }
}
