package ru.roguelike.view;

import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.List;

public interface ConsoleView {
    void clear();
    DrawingResult draw(List<List<Drawable>> figure, List<String> info, List<String> log,
              boolean showHelpScreen, boolean loadMapFromFile) throws IOException;
    Screen getScreen();
}
