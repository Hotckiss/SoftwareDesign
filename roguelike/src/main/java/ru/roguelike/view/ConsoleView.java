package ru.roguelike.view;

import com.googlecode.lanterna.screen.Screen;

import java.util.List;

/**
 * A console where game happens.
 */
public interface ConsoleView {
    /**
     * Clears the console.
     */
    void clear();

    /**
     * Draws a given board on the console.
     *
     * @param figure         is a figure to be drawn.
     * @param info           is a general info about possible actions.
     * @param log            is a current game log.
     * @param showHelpScreen is true if we want to show help screen.
     */
    void draw(List<List<Drawable>> figure, List<String> info, List<String> log, boolean showHelpScreen);

    /**
     * @return a current screen.
     */
    Screen getScreen();
}
