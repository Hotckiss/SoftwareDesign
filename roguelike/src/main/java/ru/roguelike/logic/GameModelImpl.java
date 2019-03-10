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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameModelImpl implements GameModel {
    private List<List<AbstractGameObject>> fieldModel;
    private Player player;
    private FinalKey key;
    private List<AbstractGameParticipant> mobs;
    private List<AbstractArtifact> artifacts;
    private List<String> gameLog = new ArrayList<>();

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
    public List<String> getInfo() {
        List<String> info = new ArrayList<>();
        info.add("Game INFO:");
        info.add("P - player");
        info.add("k - key to win");
        info.add("...");

        return info;
    }

    @Override
    public List<String> getLog() {
        List<String> gameSituation = new ArrayList<>();
        gameSituation.add("Player health: " + String.valueOf(player.getHealth()));

        for (AbstractGameParticipant mob: mobs) {
            gameSituation.add("Mob health: " + String.valueOf(mob.getHealth()));
        }

        if (!player.isAlive()) {
            gameSituation.add("You lose!");
        }

        return gameSituation;
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

        if (!player.isAlive()) {
            gameLog.add("You lose!");
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
        if (pos.getY() == to.getY() && pos.getX() == to.getX()) {
            return;
        }

        for (AbstractGameParticipant opponent: mobs) {
            if (opponent.getPosition().getX() == to.getX() &&
            opponent.getPosition().getY() == to.getY()) {
                attack(participant, opponent);
                if (opponent.isAlive()) {
                    return;
                } else {
                    gameLog.add("You have killed mob!");
                }
            }
        }

        if (player.getPosition().getX() == to.getX() &&
                player.getPosition().getY() == to.getY()) {
            attack(participant, player);
            if (player.isAlive()) {
                return;
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
        if (attacker instanceof Player && !(defender instanceof Player)) {
            gameLog.add("Player attacks mob!");
            attacker.hit(defender);
        } else if (!(attacker instanceof Player) && defender instanceof Player) {
            gameLog.add("Mob attacks player!");
            attacker.hit(defender);
        }
    }
}
