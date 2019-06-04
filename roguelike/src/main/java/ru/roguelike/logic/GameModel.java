package ru.roguelike.logic;

import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.artifacts.FinalKey;
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

    List<AbstractGameParticipant> getMobs();

    List<Artifact> getArtifacts();

    FinalKey getKey();

    Player getPlayer();

    Integer addPlayer(Player newPlayer);
    void nextActivePlayer();
    void removePlayer(Integer id);
    Integer addPlayerRandom();
    Integer getActivePlayerId();
}
