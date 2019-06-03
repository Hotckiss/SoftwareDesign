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
     * @param figure is a figure to be drawn.
     * @param info   is a general info about possible actions.
     * @param log    is a current game log.
     */
    <T extends Drawable> void draw(List<List<T>> figure, List<String> info, List<String> log)
            throws IOException;

    /**
     * @return a current screen.
     */
    Screen getScreen();

    /**
     * Shows menu and returns user selection in the menu
     *
     * @return user selection in the menu
     */
    String showMenu(String[] menuOptions) throws IOException;

    /**
     * Takes a file name from user.
     *
     * @return a file name from which a map will be loaded.
     * @throws IOException if it occurs
     */
    String getMapFileName() throws IOException;

    /**
     * Draws help screen.
     */
    void drawHelpScreen();
}
