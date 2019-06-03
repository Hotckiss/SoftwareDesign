package ru.roguelike.logic;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TabBehaviour;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.screen.VirtualScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.junit.Test;
import ru.roguelike.logic.generators.FromFileGenerator;
import ru.roguelike.view.Drawable;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class GameModelImplTest {

    @Test
    public void testMakeActionSimple() throws IOException {
        KeyStroke keyStroke = new KeyStroke('a', false, false, false);
        MockScreen screen = new MockScreen(keyStroke);

        String fileName = "src/maps/map1.txt";
        int fieldWidth = 9;

        FromFileGenerator generator = new FromFileGenerator(fileName);
        GameModel model = generator.generate();

        model.makeAction(screen);

        StringBuilder actualField = new StringBuilder();
        for (List<Drawable> drawableList : model.makeDrawable()) {
            for (Drawable drawable : drawableList) {
                actualField.append(drawable.getDrawingFigure());
            }

            actualField.append("\n");
        }

        assertEquals('P', actualField.toString().charAt(2 * (fieldWidth + 1) + 2));
    }

    @Test
    public void testMakeActionTakeArtifact() throws IOException {
        MockScreen screen = new MockScreen(null);

        KeyStroke keyStrokeRight = new KeyStroke('d', false, false, false);
        KeyStroke keyStrokeDown = new KeyStroke('s', false, false, false);

        String fileName = "src/maps/map2.txt";
        int fieldWidth = 14;

        FromFileGenerator generator = new FromFileGenerator(fileName);
        GameModel model = generator.generate();

        screen.setKeyStroke(keyStrokeRight);
        model.makeAction(screen);
        screen.setKeyStroke(keyStrokeDown);
        model.makeAction(screen);

        StringBuilder actualField = new StringBuilder();
        for (List<Drawable> drawableList : model.makeDrawable()) {
            for (Drawable drawable : drawableList) {
                actualField.append(drawable.getDrawingFigure());
            }

            actualField.append("\n");
        }

        assertEquals('P', actualField.toString().charAt((fieldWidth + 1) + 2));
    }

    class MockScreen implements Screen {
        private KeyStroke keyStroke;

        MockScreen(KeyStroke keyStroke) {
            this.keyStroke = keyStroke;
        }

        public void setKeyStroke(KeyStroke keyStroke) {
            this.keyStroke = keyStroke;
        }

        @Override
        public void startScreen() throws IOException {

        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public void stopScreen() throws IOException {

        }

        @Override
        public void clear() {

        }

        @Override
        public TerminalPosition getCursorPosition() {
            return null;
        }

        @Override
        public void setCursorPosition(TerminalPosition position) {

        }

        @Override
        public TabBehaviour getTabBehaviour() {
            return null;
        }

        @Override
        public void setTabBehaviour(TabBehaviour tabBehaviour) {

        }

        @Override
        public TerminalSize getTerminalSize() {
            return null;
        }

        @Override
        public void setCharacter(int column, int row, TextCharacter screenCharacter) {

        }

        @Override
        public void setCharacter(TerminalPosition position, TextCharacter screenCharacter) {

        }

        @Override
        public TextGraphics newTextGraphics() {
            return null;
        }

        @Override
        public TextCharacter getFrontCharacter(int column, int row) {
            return null;
        }

        @Override
        public TextCharacter getFrontCharacter(TerminalPosition position) {
            return null;
        }

        @Override
        public TextCharacter getBackCharacter(int column, int row) {
            return null;
        }

        @Override
        public TextCharacter getBackCharacter(TerminalPosition position) {
            return null;
        }

        @Override
        public void refresh() throws IOException {

        }

        @Override
        public void refresh(RefreshType refreshType) throws IOException {

        }

        @Override
        public TerminalSize doResizeIfNecessary() {
            return null;
        }

        @Override
        public void scrollLines(int firstLine, int lastLine, int distance) {

        }

        @Override
        public KeyStroke pollInput() throws IOException {
            return null;
        }

        @Override
        public KeyStroke readInput() throws IOException {
            return keyStroke;
        }
    }
}