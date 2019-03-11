package ru.roguelike.view;

import com.googlecode.lanterna.screen.Screen;

import java.util.List;

public interface ConsoleView {
    void clear();
    void draw(List<List<Drawable>> figure, List<String> info, List<String> log, boolean showHelpScreen);
    Screen getScreen();
}
