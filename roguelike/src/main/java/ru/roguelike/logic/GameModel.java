package ru.roguelike.logic;

import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.UserInputProvider;

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
     * @param provider is
     * @throws IOException if it occurs
     */
    void makeMove(UserInputProvider provider) throws IOException;

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

    List<AbstractGameParticipant> getMobs();

    List<Artifact> getArtifacts();

    FinalKey getKey();

    Player getPlayer();

    boolean isSavedGameEqualToCurrent();

    void setSavedGameEqualToCurrent(boolean savedGameEqualToCurrent);

}
