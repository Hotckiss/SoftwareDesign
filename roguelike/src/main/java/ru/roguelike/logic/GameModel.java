package ru.roguelike.logic;

import com.googlecode.lanterna.input.KeyStroke;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.base.AbstractArtifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.Drawable;

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
     * @return a general info about the game
     */
    List<String> getInfo();

    /**
     * @return a current log of the game
     */
    List<String> getLog();

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
     * @return true if "loading map from file" mode is on
     */
    boolean isLoadMapFromFile();

    /**
     * Sets true if there was an error while loading map
     *
     * @param errorWhileLoadingMap true if there was an error while loading map
     */
    void setErrorWhileLoadingMap(boolean errorWhileLoadingMap);

    /**
     * Sets true if "loading map from file" mode is on
     *
     * @param loadMapFromFile is true if "loading map from file" mode is on
     */
    void setLoadMapFromFile(boolean loadMapFromFile);

    /**
     * Returns an array of start menu options
     *
     * @return an array of start menu options
     */
    String[] getStartMenuOptions();

    List<AbstractGameParticipant> getMobs();

    List<AbstractArtifact> getArtifacts();

    FinalKey getKey();

    Player getPlayer();

    /**
     * Starts the game in the desired way.
     *
     * @param selection user menu selection
     * @param error
     */
    GameModel startGameFromSelection(String selection, String error) throws Exception;

    boolean isSavedGameEqualToCurrent();

    void setSavedGameEqualToCurrent(boolean savedGameEqualToCurrent);

}
