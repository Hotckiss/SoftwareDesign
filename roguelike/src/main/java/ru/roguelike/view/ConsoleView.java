package ru.roguelike.view;

import ru.roguelike.logic.MenuOption;

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
     * Shows menu and returns user selection in the menu
     *
     * @return user selection in the menu
     */
    MenuOption showMenu() throws IOException;

    /**
     * Shows online menu
     */
    String showOnlineMenu() throws IOException;

    /**
     * Displays list of sessions from server
     * @param sessionsList sessions to list
     * @throws IOException if any I/O error occurred
     */
    void showSessionsList(String sessionsList) throws IOException;

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

    /**
     * Method for refreshing game screen
     */
    void refreshScreen();

    /**
     * Method for reading input from screen via input provider
     */
    UserInputProvider makeInputProvider() throws IOException;

    /**
     * Method for reading line from screen
     * @return input string
     * @throws IOException if any I/O error occurred
     */
    String readLineFromScreen() throws IOException;
}
