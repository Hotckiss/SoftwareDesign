package ru.roguelike.models.objects.movable;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.view.UserInputProvider;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Represents a player.
 */
public class Player extends AbstractGameParticipant {
    private ArrayDeque<ArtifactItem> artifacts = new ArrayDeque<>();

    /**
     * Constructs new Player on specified position
     * @param position position to add Player
     */
    public Player(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 100;
        this.health = 100;
        this.physicalDamage = 20;
        this.fireDamage = 5;
        this.freezeProbability = 0;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 5;
        this.experience = 0;
        this.freezeCount = 0;
    }

    /**
     * Method to add new artifact to player equipment
     * @param artifact artifact to add
     */
    public void addArtifact(AbstractArtifact artifact) {
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

    /**
     * The moves can be (relevant keys are given):
     * w - up,
     * a - left,
     * s - down,
     * d - right,
     * e - enable artifact,
     * q - disable artifact.
     *
     * @param model -- is a current game state
     * @return a move corresponding to the user's action.
     * @throws IOException if it occurs
     */
    @Override
    @NotNull
    public Move move(@NotNull UserInputProvider provider, GameModel model) throws
            IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        List<List<AbstractGameObject>> field = model.getField();
        int x = position.getX();
        int y = position.getY();

        if (provider.getCharacter() == null) {
            return Move.NONE;
        }

        switch (provider.getCharacter()) {
            case 'w':
                return (x > 0 && !(field.get(x - 1).get(y) instanceof Wall)) ? Move.UP : Move.NONE;
            case 'a':
                return (y > 0 && !(field.get(x).get(y - 1) instanceof Wall)) ? Move.LEFT : Move.NONE;
            case 's':
                return (x + 1 < field.size() && !(field.get(x + 1).get(y) instanceof Wall)) ? Move.DOWN : Move.NONE;
            case 'd':
                return (y + 1 < field.get(0).size() && !(field.get(x).get(y + 1) instanceof Wall)) ? Move.RIGHT : Move.NONE;
            case 'e':
                if (!artifacts.isEmpty() && !artifacts.getFirst().equipped()) {
                    artifacts.getFirst().equip();
                    enableArtifact(artifacts.getFirst().getItem());
                }

                return Move.NONE;
            case 'q':
                if (!artifacts.isEmpty() && artifacts.getFirst().equipped()) {
                    artifacts.getFirst().disable();
                    disableArtifact(artifacts.getFirst().getItem());
                }

                return Move.NONE;
        }

        return Move.NONE;
    }

    public ArrayDeque<ArtifactItem> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(ArrayDeque<ArtifactItem> artifacts) {
        this.artifacts = artifacts;
    }

    public static class ArtifactItem {
        private AbstractArtifact item;
        private boolean isEquipped;

        public ArtifactItem(AbstractArtifact artifact) {
            item = artifact;
            isEquipped = false;
        }

        public AbstractArtifact getItem() {
            return item;
        }

        private void setEquipped(boolean isEquipped) {
            this.isEquipped = isEquipped;
        }

        public void equip() {
            setEquipped(true);
        }

        public void disable() {
            setEquipped(false);
        }

        public boolean equipped() {
            return isEquipped;
        }
    }

    public int getExperience() {
        return 0;
    }
}
