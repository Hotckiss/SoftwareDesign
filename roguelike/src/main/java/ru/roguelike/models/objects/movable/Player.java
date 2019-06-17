package ru.roguelike.models.objects.movable;

import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameParticipant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player.
 */
public class Player extends AbstractGameParticipant implements Serializable {
    private List<ArtifactInInventory> artifacts = new ArrayList<>();

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
        artifacts.add(0, new ArtifactInInventory(artifact));
    }

    /**
     * Gets view model of current player artifacts
     * @return view model of current player artifacts
     */
    public String getArtifactsLog(boolean showEnabled) {
        StringBuilder builder = new StringBuilder();

        for (ArtifactInInventory item : artifacts) {
            builder.append(item.getItem().getDrawingFigure());
            builder.append(showEnabled ? (item.equipped() ? "(+) " : "(-) ") : " ");
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

    public List<ArtifactInInventory> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<ArtifactInInventory> artifacts) {
        this.artifacts = artifacts;
    }

    /**
     * Wrapper for artifact
     */
    public static class ArtifactInInventory implements Serializable {
        private Artifact item;
        private boolean isEquipped;

        /**
         * Constructor to wrap artifact
         * @param artifact artifact to wrap
         */
        public ArtifactInInventory(Artifact artifact) {
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
