package ru.roguelike.logic;

import com.googlecode.lanterna.screen.Screen;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.view.Drawable;

import java.io.IOException;
import java.util.List;

public interface GameModel {
    List<List<Drawable>> makeDrawable();
    List<List<AbstractGameObject>> getField();
    void makeMove(Screen screen) throws IOException;
}
