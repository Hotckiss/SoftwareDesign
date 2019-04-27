package ru.roguelike.logic;

import com.googlecode.lanterna.screen.Screen;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.base.AbstractArtifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.Drawable;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

/**
 * Represents a model (current state) of the game.
 */
public interface GameModel {
    /**
     * @return if the game is finished
     */
    boolean finished();

    /**
     * @return a figures to draw on board.
     */
    List<List<Drawable>> makeDrawable();

    /**
     * @return a general info about the game
     */
    List<String> getInfo();

    /**
     * @return a current log of the game
     */
    List<String> getLog();

    void AddToGameLog(String log);

    /**
     * @return returns a field
     */
    List<List<AbstractGameObject>> getField();

    /**
     * Takes an input from user and makes a corresponding move.
     *
     * @param keyStroke is
     * @throws IOException if it occurs
     */
    void makeMove(KeyStroke keyStroke) throws IOException;

    /**
     * @return if a help screen should be shown
     */
    boolean isShowHelpScreen();

    /**
     * @return true if "loading map from file" mode is on
     */
    boolean isLoadMapFromFile();

    /**
     * Sets true if there was an error while loading map
     * @param errorWhileLoadingMap true if there was an error while loading map
     */
    void setErrorWhileLoadingMap(boolean errorWhileLoadingMap);

    /**
     * Sets true if "loading map from file" mode is on
     * @param loadMapFromFile is true if "loading map from file" mode is on
     */
    void setLoadMapFromFile(boolean loadMapFromFile);

    /**
     * Returns an array of start menu options
     * @return an array of start menu options
     */
    String[] getStartMenuOptions();

    /**
     * Starts the game in the desired way.
     * @param selection user menu selection
     * @param error
     */
    void startGameFromSelection(String selection);

   List<AbstractGameParticipant> getMobs();

   boolean isValidPosition(@NotNull Position position);

   List<AbstractArtifact> getArtifacts();

   Position getKeyPosition();

   FinalKey getKey();

   void setFinished(boolean finished);

   Player getPlayer();

    GameModel startGameFromSelection(String selection, String error) throws Exception;

    List<List<AbstractGameObject>> getFieldModel();

    Player getPlayer();

    FinalKey getKey();

    List<AbstractArtifact> getArtifacts();

    List<AbstractGameParticipant> getMobs();

    boolean isSavedGameEqualToCurrent();

    void setSavedGameEqualToCurrent(boolean savedGameEqualToCurrent);
}
