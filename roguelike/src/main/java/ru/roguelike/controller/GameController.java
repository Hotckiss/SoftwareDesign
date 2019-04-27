package ru.roguelike.controller;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.commands.*;
import ru.roguelike.logic.generators.FromFileGenerator;
import ru.roguelike.view.ConsoleView;
import ru.roguelike.view.DrawingResult;

import java.io.IOException;

/**
 * Controls the game process.
 */
public class GameController {
    private GameModel game;
    private ConsoleView view;

    public GameController(@NotNull GameModel model, @NotNull ConsoleView view) {
        this.game = model;
        this.view = view;
    }

    /**
     * Starts the game process
     *
     * @throws IOException if it occurs during user's input reading.
     */
    public void runGame() throws IOException {
        RoguelikeLogger.INSTANCE.log_info("Game started");

        String[] menuOptions = game.getStartMenuOptions();
        String selection = view.showMenu(menuOptions);

        game.startGameFromSelection(selection);

        while (!game.finished()) {
            view.clear();
            DrawingResult result = view.draw(game.makeDrawable(), game.getInfo(), game.getLog(), game.isShowHelpScreen(), game.isLoadMapFromFile());

            if (result.isLoadMapFromFile()) {
                FromFileGenerator generator = new FromFileGenerator(result.getFileNameForMap());
                GameModel newModel = generator.generate();

                if (newModel == null) {
                    game.setErrorWhileLoadingMap(true);
                    game.setLoadMapFromFile(false);
                } else {
                    this.game = newModel;
                }

                view.clear();
                result = view.draw(game.makeDrawable(), game.getInfo(), game.getLog(), game.isShowHelpScreen(), game.isLoadMapFromFile());
            }

            game.makeAction(view.getScreen());
        }
        RoguelikeLogger.INSTANCE.log_info("Game finished");
        view.clear();
        view.draw(game.makeDrawable(), game.getInfo(), game.getLog(), game.isShowHelpScreen(), game.isLoadMapFromFile());
    }

    private class Invoker {
        public void makeAction() throws IOException {
            Screen screen = GameController.this.view.getScreen();
            screen.refresh();
            KeyStroke keyStroke = screen.readInput();
            RoguelikeLogger.INSTANCE.log_info("Input from user: " + keyStroke.getCharacter());
            Command command = createCommand(keyStroke);
            if (command != null) {
                command.execute();
            }
        }

        @Nullable
        private Command createCommand(KeyStroke keyStroke) {
            if (keyStroke.getCharacter() == null) {
                return null;
            }
            switch (keyStroke.getCharacter()) {
                case 'l':
                    return new LoadMapCommand(GameController.this.view);
                case 'h':
                    return new ShowHelpScreenCommand();
                case 'r':
                    return new HideHelpScreenCommand();
                default:
                    return new ApplyMoveCommand(keyStroke,
                            GameController.this.game);
            }
        }

    }
}
