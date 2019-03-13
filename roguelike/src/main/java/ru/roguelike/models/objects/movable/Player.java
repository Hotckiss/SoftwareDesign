package ru.roguelike.models.objects.movable;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.Wall;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Represents a player.
 */
public class Player extends AbstractGameParticipant {
    private ArrayDeque<ArtifactItem> artifacts = new ArrayDeque<>();

    public Player(Position position) {
        this.position = position;
        this.isAvailable = false;

        this.fullHealth = 100;
        this.health = 100;
        this.physicalDamage = 20;
        this.fireDamage = 0;
        this.freezeProbability = 0;
        this.fireProbability = 0;
        this.physicalDamageMultiplier = 1;
        this.fireDamageMultiplier = 1;
        this.regeneration = 5;
        this.freezeCount = 0;
    }

    public void addArtifact(AbstractArtifact artifact) {
        artifacts.addFirst(new ArtifactItem(artifact));
    }

    public String getArtifactsLog() {
        StringBuilder builder = new StringBuilder();

        for (ArtifactItem item : artifacts) {
            builder.append(item.getItem().getDrawingFigure());
            builder.append(' ');
        }

        return builder.toString();
    }

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
    public Move move(@NotNull KeyStroke keyStroke, GameModel model) throws
            IOException {
        if (freezeCount > 0) {
            return Move.NONE;
        }

        if (keyStroke.getKeyType() == KeyType.Escape) {
            return Move.NONE;
        }

        List<List<AbstractGameObject>> field = model.getField();
        int x = position.getX();
        int y = position.getY();

        if (keyStroke.getCharacter() == null) {
            return Move.NONE;
        }

        //TODO: apply artifact better
        switch (keyStroke.getCharacter()) {
            case 'w':
                return (x > 0 && !(field.get(x - 1).get(y) instanceof Wall)) ? Move.TOP : Move.NONE;
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

    private class ArtifactItem {
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
}
