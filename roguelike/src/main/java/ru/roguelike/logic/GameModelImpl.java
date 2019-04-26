package ru.roguelike.logic;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import org.jetbrains.annotations.NotNull;
import ru.roguelike.RoguelikeLogger;
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

/**
 * {@inheritDoc}
 */
public class GameModelImpl implements GameModel {
    private List<List<AbstractGameObject>> fieldModel;
    private Player player;
    private FinalKey key;
    private List<AbstractGameParticipant> mobs;
    private List<AbstractArtifact> artifacts;
    private List<String> gameLog = new ArrayList<>();
    private boolean isFinished = false;
    private boolean showHelpScreen = false;
    private boolean loadMapFromFile = false;
    private boolean errorWhileLoadingMap = false;
    private String[] startMenuOptions = {"Start new game", "Load saved game"};

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
    public void startGameFromSelection(String selection) {
        switch (selection) {
            case "Start new game":
                return;
            case "Load saved game":
                // TODO(alina): Loading game
                return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean finished() {
        return isFinished;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<Drawable>> makeDrawable() {
        return (List) fieldModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getInfo() {
        List<String> info = new ArrayList<>();
        info.add("Game INFO:");
        info.add("P - player, k - key to win");
        info.add("Press h for more info");
        info.add("Press l for loading map from file");
        info.add("");

        return info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getLog() {
        List<String> gameSituation = new ArrayList<>();

        gameSituation.add("Health: " + player.getHealth() + " Items: " + player.getArtifactsLog());

        StringBuilder mobsHealth = new StringBuilder();
        mobsHealth.append("Mobs' health: ");

        for (AbstractGameParticipant mob : mobs) {
            mobsHealth.append(String.valueOf(mob.getHealth()));
            mobsHealth.append(" ");
        }

        gameSituation.add(mobsHealth.toString());

        if (!player.isAlive() && isFinished) {
            gameSituation.add("You lose!");
        }

        if (player.isAlive() && isFinished) {
            gameSituation.add("You win!");
        }
        RoguelikeLogger.INSTANCE.log_info(String.join(System.lineSeparator(), gameSituation));


        if (errorWhileLoadingMap) {
            gameSituation.add("Error while loading map!");
            errorWhileLoadingMap = false;
        }

        return gameSituation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<AbstractGameObject>> getField() {
        return fieldModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeAction(@NotNull Screen screen) throws IOException {
        screen.refresh();
        KeyStroke keyStroke = screen.readInput();

        RoguelikeLogger.INSTANCE.log_info("Input from user: " + keyStroke.getCharacter());

        if (keyStroke.getCharacter() != null) {
            if (keyStroke.getCharacter().equals('h')) {
                showHelpScreen = true;
            }
            if (keyStroke.getCharacter().equals('r')) {
                showHelpScreen = false;
            }
            if (keyStroke.getCharacter().equals('l')) {
                loadMapFromFile = true;
            }
        }

        if (loadMapFromFile) {
            return;
        }

        if (showHelpScreen) {
            return;
        }

        Move playerMove = player.move(keyStroke, this);
        RoguelikeLogger.INSTANCE.log_info("Move " + playerMove);
        applyMove(player, playerMove);

        mobs = mobs.stream().filter(AbstractGameParticipant::isAlive).collect(Collectors.toList());

        for (AbstractGameParticipant mob : mobs) {
            Move to = mob.move(keyStroke, this);
            RoguelikeLogger.INSTANCE.log_info("Mob move " + to + " from position " + mob.getPosition().getX()
                    + " " + mob.getPosition().getY());
            applyMove(mob, to);
        }

        if (!player.isAlive()) {
            RoguelikeLogger.INSTANCE.log_info("Lose");
            gameLog.add("You lose!");
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
            isFinished = true;
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

        for (AbstractGameParticipant opponent : mobs) {
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

        if (participant instanceof Player) {
            for (AbstractArtifact artifact : artifacts) {
                if (artifact.getPosition().getX() == to.getX() && artifact.getPosition().getY() == to.getY() && !artifact.taken()) {
                    player.addArtifact(artifact);
                    artifact.take();
                    break;
                }
            }

            if (key.getPosition().getX() == to.getX() && key.getPosition().getY() == to.getY()) {
                player.addArtifact(key);
                isFinished = true;
                gameLog.add("Player gets a key and wins");
            }
        }


        if (player.getPosition().getX() == to.getX() &&
                player.getPosition().getY() == to.getY()) {
            attack(participant, player);
            if (player.isAlive()) {
                return;
            } else {
                isFinished = true;
            }
        }

        fieldModel.get(pos.getX()).set(pos.getY(), new FreePlace(pos));
        fieldModel.get(to.getX()).set(to.getY(), participant);
        participant.setPosition(to);
    }

    private boolean isValidPosition(@NotNull Position position) {
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

    public boolean isShowHelpScreen() {
        return showHelpScreen;
    }

    public void setShowHelpScreen(boolean showHelpScreen) {
        this.showHelpScreen = showHelpScreen;
    }

    public boolean isLoadMapFromFile() {
        return loadMapFromFile;
    }

    public void setLoadMapFromFile(boolean loadMapFromFile) {
        this.loadMapFromFile = loadMapFromFile;
    }

    public boolean isErrorWhileLoadingMap() {
        return errorWhileLoadingMap;
    }

    public void setErrorWhileLoadingMap(boolean errorWhileLoadingMap) {
        this.errorWhileLoadingMap = errorWhileLoadingMap;
    }

    @Override
    public String[] getStartMenuOptions() {
        return startMenuOptions;
    }
}
