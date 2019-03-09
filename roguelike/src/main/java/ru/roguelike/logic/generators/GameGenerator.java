package ru.roguelike.logic.generators;

import ru.roguelike.logic.GameModel;
import ru.roguelike.models.objects.base.AbstractGameObject;

import java.util.List;

public interface GameGenerator {
    GameModel generate();
}
