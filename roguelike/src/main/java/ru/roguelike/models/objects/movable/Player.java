package ru.roguelike.models.objects.movable;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Represents a player.
 */
public class Player extends AbstractGameParticipant implements Serializable {
    private ArrayDeque<ArtifactItem> artifacts = new ArrayDeque<>();

    /**
     * Constructs new Player on specified position
     * @param position position to add Player
     */
    public Player(Position position) {
        super(position,
                100,
                20,
                5,
                0,
                0,
                1,
                1,
                5,
                'P');
        this.isAvailable = false;
    }

    /**
     * Method to add new artifact to player equipment
     * @param artifact artifact to add
     */
    public void addArtifact(Artifact artifact) {
        artifacts.addFirst(new ArtifactItem(artifact));
    }

    /**
     * Gets view model of current player artifacts
     * @return view model of current player artifacts
     */
    public String getArtifactsLog() {
        StringBuilder builder = new StringBuilder();

        for (ArtifactItem item : artifacts) {
            builder.append(item.getItem().getDrawingFigure());
            builder.append(' ');
        }

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Character getDrawingFigure() {
        return 'P';
    }

    public ArrayDeque<ArtifactItem> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(ArrayDeque<ArtifactItem> artifacts) {
        this.artifacts = artifacts;
    }

    /**
     * Wrapper for artifact
     */
    public static class ArtifactItem implements Serializable {
        private Artifact item;
        private boolean isEquipped;

        /**
         * Constructor to wrap artifact
         * @param artifact artifact to wrap
         */
        public ArtifactItem(Artifact artifact) {
            item = artifact;
            isEquipped = false;
        }

        /**
         * Getter for real artifact
         * @return real artifact
         */
        public Artifact getItem() {
            return item;
        }

        /**
         * Method for equipping artifact
         */
        public void equip() {
            setEquipped(true);
        }

        /**
         * Method for disabling artifact
         */
        public void disable() {
            setEquipped(false);
        }

        /**
         * Check that artifact equipped
         * @return true if artifact is equipped false otherwise
         */
        public boolean equipped() {
            return isEquipped;
        }

        private void setEquipped(boolean isEquipped) {
            this.isEquipped = isEquipped;
        }
    }

    /**
     * Get experience for killing player. Zero because mobs do not receive experience
     * @return experience
     */
    public int getExperience() {
        return 0;
    }
}
