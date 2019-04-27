package ru.roguelike.view;

import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
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
     * @param loadMapFromFile is true if want to load a map from file.
     * @return DrawingResult for current drawing process.
     */
    DrawingResult draw(List<List<Drawable>> figure, List<String> info, List<String> log,
                       boolean showHelpScreen, boolean loadMapFromFile) throws IOException;

    /**
     * @return a current screen.
     */
    Screen getScreen();

    /**
     * Shows menu and returns user selection in the menu
     * @return user selection in the menu
     */
    String showMenu(String[] menuOptions) throws IOException;

    String getMapFileName() throws IOException;
    String showMenu(String[] menuOptions, String error) throws IOException;
}
