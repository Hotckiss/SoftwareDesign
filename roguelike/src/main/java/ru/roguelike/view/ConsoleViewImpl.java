package ru.roguelike.view;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import ru.roguelike.info.GameInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class ConsoleViewImpl implements ConsoleView {
    private List<List<Character>> field = new ArrayList<>();
    private List<String> info = new ArrayList<>();
    private List<String> log = new ArrayList<>();
    private Screen gameScreen;

    public ConsoleViewImpl() {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal terminal;
        Screen screen;

        try {
            terminal = defaultTerminalFactory.createTerminal();
            screen = new TerminalScreen(terminal);

            this.gameScreen = screen;

            gameScreen.startScreen();
        } catch (IOException e) {
            //TODO: handle
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        //TODO: clear logs if needed (do not clear game log)
        field.clear();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DrawingResult draw(List<List<Drawable>> figures, List<String> info, List<String> log,
                     boolean showHelpScreen, boolean loadMapFromFile) throws IOException {
        String fileName = null;
        
        if (loadMapFromFile) {
            TerminalPosition startCursorPosition = gameScreen.getCursorPosition();
            String text = "Enter filename: ";

            for (int i = startCursorPosition.getColumn(); i < startCursorPosition.getColumn() + text.length(); i++) {
                gameScreen.setCharacter(i, startCursorPosition.getRow(),
                        new TextCharacter(text.charAt(i - startCursorPosition.getColumn())));
            }

            gameScreen.setCursorPosition(new TerminalPosition(startCursorPosition.getColumn() + text.length(),
                    startCursorPosition.getRow()));

            try {
                gameScreen.refresh();
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileName = getFileName(gameScreen.getCursorPosition());
        }
        if (showHelpScreen) {
            drawInfo(GameInfo.getInfo());
        } else {
            figures.forEach(figuresRow -> field.add(mapRow(figuresRow)));
            this.info = info;
            this.log = log;

            drawInner();
        }

        return new DrawingResult(loadMapFromFile, fileName);
    }

    private String getFileName(TerminalPosition cursorPosition) throws IOException {
        KeyStroke keyStroke = gameScreen.readInput();
        StringBuilder fileName = new StringBuilder();

        while (keyStroke.getKeyType() != KeyType.Enter) {
            if (keyStroke.getCharacter() != null && keyStroke.getKeyType() != KeyType.Delete) {
                fileName.append(keyStroke.getCharacter());
                gameScreen.setCharacter(cursorPosition.getColumn(), cursorPosition.getRow(),
                        new TextCharacter(keyStroke.getCharacter()));
                gameScreen.setCursorPosition(new TerminalPosition(cursorPosition.getColumn() + 1,
                        cursorPosition.getRow()));
                cursorPosition = gameScreen.getCursorPosition();

                try {
                    gameScreen.refresh();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            keyStroke = gameScreen.readInput();
        }
        
        return fileName.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Screen getScreen() {
        return gameScreen;
    }

    private void drawInner() {
        gameScreen.clear();
        for (int i = 0; i < field.size(); i++) {
            List<Character> row = field.get(i);

            for (int j = 0; j < row.size(); j++) {
                gameScreen.setCharacter(j, i, new TextCharacter(row.get(j)));
            }
        }

        for (int i = 0; i < info.size(); i++) {
            for (int j = 0; j < info.get(i).length(); j++) {
                gameScreen.setCharacter(j, field.size() + i, new TextCharacter(info.get(i).charAt(j)));
            }
        }

        for (int i = 0; i < log.size(); i++) {
            for (int j = 0; j < log.get(i).length(); j++) {
                gameScreen.setCharacter(j, field.size() + info.size() + i, new TextCharacter(log.get(i).charAt(j)));
            }
        }

        gameScreen.setCursorPosition(new TerminalPosition(0, field.size() + info.size() + log.size()));

        try {
            gameScreen.refresh();
        } catch (IOException e) {
            //TODO:
            e.printStackTrace();
        }
    }

    private void drawInfo(List<String> info) {
        gameScreen.clear();

        for (int i = 0; i < info.size(); i++) {
            for (int j = 0; j < info.get(i).length(); j++) {
                gameScreen.setCharacter(j, i, new TextCharacter(info.get(i).charAt(j)));
            }
        }
    }

    private List<Character> mapRow(List<Drawable> row) {
        List<Character> result = new ArrayList<>();

        row.forEach(drawable -> result.add(drawable.getDrawingFigure()));

        return result;
    }
}
