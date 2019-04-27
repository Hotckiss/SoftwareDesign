package ru.roguelike.logic.commands;

import com.googlecode.lanterna.input.KeyStroke;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.Move;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractArtifact;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.movable.Player;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class ApplyMoveCommand implements Command {
    private final Player player;
    private final KeyStroke keyStroke;
    private final GameModel model;

    public ApplyMoveCommand(KeyStroke keyStroke, GameModel
            model) {
        this.player = model.getPlayer();
        this.keyStroke = keyStroke;
        this.model = model;
    }

    @Override
    public void execute() throws IOException {
        Move playerMove = player.move(keyStroke, model);
        RoguelikeLogger.INSTANCE.log_info("Move " + playerMove);
        applyMove(player, playerMove);

        List<AbstractGameParticipant> mobs = model.getMobs();

        for (AbstractGameParticipant mob : mobs) {
            Move to = mob.move(keyStroke, model);
            RoguelikeLogger.INSTANCE.log_info("Mob move " + to + " from position " + mob.getPosition().getX()
                    + " " + mob.getPosition().getY());
            applyMove(mob, to);
        }

        if (!player.isAlive()) {
            RoguelikeLogger.INSTANCE.log_info("Lose");
            model.AddToGameLog("You lose!");
        }

        for (AbstractGameParticipant mob : mobs) {
            mob.fireStep();
            mob.regenerate();
            mob.freezeStep();
        }

        //remove burned mobs
        mobs = mobs.stream().filter(AbstractGameParticipant::isAlive).collect(Collectors.toList());

        player.fireStep();

        if (player.isAlive()) {
            player.regenerate();
            player.freezeStep();
        } else {
            model.setFinished(true);
        }
    }


    private void applyMove(@NotNull AbstractGameParticipant participant, @NotNull
            Move move) {
        Position pos = participant.getPosition();
        Position to = pos.none();

        switch (move) {
            case NONE:
                to = pos.none();
                break;
            case LEFT:
                if (model.isValidPosition(pos.left())) {
                    to = pos.left();
                }
                break;
            case RIGHT:
                if (model.isValidPosition(pos.right())) {
                    to = pos.right();
                }
                break;
            case TOP:
                if (model.isValidPosition(pos.top())) {
                    to = pos.top();
                }
                break;
            case DOWN:
                if (model.isValidPosition(pos.bottom())) {
                    to = pos.bottom();
                }
                break;
        }
        if (pos.getY() == to.getY() && pos.getX() == to.getX()) {
            return;
        }

        for (AbstractGameParticipant opponent : model.getMobs()) {
            if (opponent.getPosition().getX() == to.getX() &&
                    opponent.getPosition().getY() == to.getY()) {
                attack(participant, opponent);
                if (opponent.isAlive()) {
                    return;
                } else {
                    model.AddToGameLog("You have killed mob!");
                }
            }
        }

        if (participant instanceof Player) {
            for (AbstractArtifact artifact : model.getArtifacts()) {
                if (artifact.getPosition().getX() == to.getX() && artifact.getPosition().getY() == to.getY() && !artifact.taken()) {
                    player.addArtifact(artifact);
                    artifact.take();
                    break;
                }
            }

            if (model.getKeyPosition().equals(to)) {
                player.addArtifact(model.getKey());
                model.setFinished(true);
                model.AddToGameLog("Player gets a key and wins");
            }
        }


        if (player.getPosition().getX() == to.getX() &&
                player.getPosition().getY() == to.getY()) {
            attack(participant, player);
            if (player.isAlive()) {
                return;
            } else {
                model.setFinished(true);
            }
        }

        model.getField().get(pos.getX()).set(pos.getY(), new FreePlace(pos));
        model.getField().get(to.getX()).set(to.getY(), participant);
        participant.setPosition(to);
    }


    private void attack(AbstractGameParticipant attacker, AbstractGameParticipant defender) {
        if (attacker instanceof Player && !(defender instanceof Player)) {
            model.AddToGameLog("Player attacks mob!");
            attacker.hit(defender);
        } else if (!(attacker instanceof Player) && defender instanceof Player) {
            model.AddToGameLog("Mob attacks player!");
            attacker.hit(defender);
        }
    }


}
