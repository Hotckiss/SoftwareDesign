package ru.roguelike.logic;

import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a model (current state) of the game.
 */
public interface GameModel extends Serializable {
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
    List<String> getLog(Integer playerId);

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
     * Get list of alive mobs
     * @return list of alive mobs
     */
    List<AbstractGameParticipant> getMobs();

    /**
     * Get list of untaken artifacts
     * @return list of untaken artifacts
     */
    List<Artifact> getArtifacts();

    /**
     * Get final key info method
     * @return final key info
     */
    FinalKey getKey();

    /**
     * Get active player info method
     * @return active player
     */
    Player getActivePlayer();

    /**
     * Move turn to next player method
     */
    void nextActivePlayer();

    /**
     * Method to remove player that left game
     * @param id player to remove
     */
    void removePlayer(Integer id);

    /**
     * Add random player to game. Returns id
     * @return player id
     */
    Integer addPlayerRandom();

    /**
     * Get ID of player making turn
     * @return active player id
     */
    Integer getActivePlayerId();

    /**
     * Get player by ID
     * @return player with this id or null of player does not exist
     */
    Player getPlayerById(Integer id);
}
