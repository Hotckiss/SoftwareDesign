package ru.roguelike;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.screen.Screen;
import ru.roguelike.view.UserInputProvider;
import ru.roguelike.view.UserInputProviderImpl;

import java.io.IOException;

/**
 * Input line from terminal utility
 */
public class InputUtils {

    /**
     * Method extracting line from terminal until enter pressed
     * @param provider input provider
     * @param gameScreen terminal
     * @return resulting line
     * @throws IOException if any I/O error occurred
     */
    public static String inputLine(UserInputProvider provider, Screen gameScreen) throws IOException {
        TerminalPosition cursorPosition = gameScreen.getCursorPosition();
        StringBuilder result = new StringBuilder();
        while (!provider.isEnter()) {
            if (provider.getCharacter() != null && !provider.isBackspace()) {
                result.append(provider.getCharacter());
                gameScreen.setCharacter(cursorPosition.getColumn(), cursorPosition.getRow(),
                        new TextCharacter(provider.getCharacter()));
                gameScreen.setCursorPosition(new TerminalPosition(cursorPosition.getColumn() + 1,
                        cursorPosition.getRow()));
                cursorPosition = gameScreen.getCursorPosition();

                try {
                    gameScreen.refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            provider = new UserInputProviderImpl(gameScreen.readInput());
        }

        return result.toString();
    }
}
