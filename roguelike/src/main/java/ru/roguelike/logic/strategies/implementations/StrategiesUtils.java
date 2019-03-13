package ru.roguelike.logic.strategies.implementations;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.models.objects.base.AbstractArtifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.map.Wall;

import java.util.List;

public class StrategiesUtils {
    public static boolean availableForMob(@NotNull List<List<AbstractGameObject>> field,
                                    int x, int y) {
        return !(field.get(x).get(y) instanceof Wall || field.get(x).get(y) instanceof AbstractArtifact);
    }
}
