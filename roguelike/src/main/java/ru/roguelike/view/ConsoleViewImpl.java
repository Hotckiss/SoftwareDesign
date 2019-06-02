package ru.roguelike.view;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextCharacter;
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

    /**
     * Constructs new console view of game
     */
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
     * Shows menu and returns user selection in the menu
     * @return user selection in the menu
     */
    @Override
    public String showMenu(String[] menuOptions, String error) throws IOException {
        gameScreen.clear();

        drawOnIthLine(0, "Menu:");
        int lineNum = 1;

        for (String option : menuOptions) {
            drawOnIthLine(lineNum + 1, option + " - Press " + lineNum);
            lineNum++;
        }

        if (error != null) {
            drawOnIthLine(menuOptions.length + 4, error);
        }

        gameScreen.refresh();

        while (true) {
            UserInputProvider provider = new UserInputProviderImpl(gameScreen.readInput());

            if (provider.getCharacter() != null) {
                Character character = provider.getCharacter();

                if (Character.isDigit(character)) {
                    int num = character - '0';

                    if (0 < num && num <= menuOptions.length) {
                        return menuOptions[num - 1];
                    }
                }
            }
        }
    }

    @Override
    public void drawHelpScreen() {
        drawInfo(GameInfo.getInfo());
    }

    @Override
    public String getMapFileName() throws IOException {
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

        return getFileName(gameScreen.getCursorPosition());
    }

    private void drawOnIthLine(int line, String text) {
        for (int j = 0; j < text.length(); j++) {
            gameScreen.setCharacter(j, line, new TextCharacter(text.charAt(j)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        field.clear();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Drawable> void draw(List<List<T>> figures, List<String> info,
                  List<String> log) throws IOException {
        figures.forEach(figuresRow -> field.add(mapRow(figuresRow)));
        this.info = info;
        this.log = log;

        drawInner();
    }

    private String getFileName(TerminalPosition cursorPosition) throws IOException {
        UserInputProvider provider = new UserInputProviderImpl(gameScreen.readInput());
        StringBuilder fileName = new StringBuilder();

        while (!provider.isEnter()) {
            if (provider.getCharacter() != null && !provider.isBackspace()) {
                fileName.append(provider.getCharacter());
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
        
        return fileName.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Screen getScreen() {
        return gameScreen;
    }

    @Override
    public String showMenu(String[] menuOptions) throws IOException {
        return null;
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

    private <T extends Drawable> List<Character> mapRow(List<T> row) {
        List<Character> result = new ArrayList<>();

        row.forEach(drawable -> result.add(drawable.getDrawingFigure()));

        return result;
    }
}
