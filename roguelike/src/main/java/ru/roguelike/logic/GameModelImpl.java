package ru.roguelike.logic;

import com.googlecode.lanterna.screen.Screen;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.base.AbstractArtifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.Drawable;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GameModelImpl implements GameModel {
    private List<List<AbstractGameObject>> fieldModel;
    private Player player;
    private FinalKey key;
    private List<AbstractGameParticipant> mobs;
    private List<AbstractArtifact> artifacts;

    public GameModelImpl(List<List<AbstractGameObject>> fieldModel,
                         Player player,
                         FinalKey key,
                         List<AbstractGameParticipant> mobs,
                         List<AbstractArtifact> artifacts) {
        this.fieldModel = fieldModel;
        this.player = player;
        this.key = key;
        this.mobs = mobs;
        this.artifacts = artifacts;
    }

    @Override
    public List<List<Drawable>> makeDrawable() {
        return (List)fieldModel;
    }

    @Override
    public List<List<AbstractGameObject>> getField() {
        return fieldModel;
    }

    @Override
    public void makeMove(Screen screen) throws IOException {
        screen.refresh();
        Move playerMove = player.move(screen, this);
        applyMove(player, playerMove);

        mobs = mobs.stream().filter(AbstractGameParticipant::isAlive).collect(Collectors.toList());

        for (AbstractGameParticipant mob: mobs) {
            Move to = mob.move(screen, this);
            applyMove(mob, to);
        }
    }

    private void applyMove(AbstractGameParticipant participant, Move move) {
        Position pos = participant.getPosition();
        Position to = pos.none();

        switch (move) {
            case NONE:
                to = pos.none();
                break;
            case LEFT:
                if (isValidPosition(pos.left())) {
                    to = pos.left();
                }
                break;
            case RIGHT:
                if (isValidPosition(pos.right())) {
                    to = pos.right();
                }
                break;
            case TOP:
                if (isValidPosition(pos.top())) {
                    to = pos.top();
                }
                break;
            case DOWN:
                if (isValidPosition(pos.bottom())) {
                    to = pos.bottom();
                }
                break;
        }

        for (AbstractGameParticipant opponent: mobs) {
            if (opponent.getPosition().getX() == to.getX() &&
            opponent.getPosition().getY() == to.getY()) {
                attack(participant, opponent);
                if (opponent.isAlive()) {
                    return;
                } else {
                    System.out.println("killed");
                }
            }
        }

        fieldModel.get(pos.getX()).set(pos.getY(), new FreePlace(pos));
        fieldModel.get(to.getX()).set(to.getY(), participant);
        participant.setPosition(to);
    }

    private boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getY() >= 0 && position.getX() < fieldModel.size() && position.getY() < fieldModel.get(0).size();
    }

    private void attack(AbstractGameParticipant attacker, AbstractGameParticipant defender) {
        if (!(attacker instanceof Player || defender instanceof Player)) {
            return;
        }
        System.out.println("attack");
        attacker.hit(defender);
    }
}
