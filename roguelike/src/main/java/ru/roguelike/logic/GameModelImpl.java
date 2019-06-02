package ru.roguelike.logic;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.logic.gameloading.GameSaverAndLoader;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.movable.Mob;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.UserInputProvider;

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
    private List<Artifact> artifacts;
    private boolean isFinished = false;
    private boolean showHelpScreen = false;
    private boolean loadMapFromFile = false;
    private boolean errorWhileLoadingMap = false;
    private String[] startMenuOptions = {"Start new game", "Load saved game"};
    private boolean isSavedGameEqualToCurrent = false;

    public GameModelImpl(List<List<AbstractGameObject>> fieldModel,
                         Player player,
                         FinalKey key,
                         List<AbstractGameParticipant> mobs,
                         List<Artifact> artifacts) {
        this.fieldModel = fieldModel;
        this.player = player;
        this.key = key;
        this.mobs = mobs;
        this.artifacts = artifacts;
    }

    @Override
    public GameModel startGameFromSelection(String selection, String error) throws Exception {
        switch (selection) {
            case "Start new game":
                return this;
            case "Load saved game":
                GameSaverAndLoader saverAndLoader = new GameSaverAndLoader();
                GameModel newGame = saverAndLoader.loadGame();
                if (newGame == null) {
                    throw new Exception("There is not any saved games!");
                }
                return newGame;
        }

        return null;
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
    public List<String> getInfo() {
        List<String> info = new ArrayList<>();
        info.add("Game INFO:");
        info.add("P - player, k - key to win");
        info.add("Press h for more info");
        info.add("Press l for loading map from file, v for saving game");
        info.add("");

        return info;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getLog() {
        List<String> gameSituation = new ArrayList<>();

        gameSituation.add("Health: " + player.getHealth() +
                " Exp: " + player.exp() + " Level: " +
                player.getLevel() + " Items: " + player.getArtifactsLog());

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
    public void makeMove(@NotNull UserInputProvider provider) throws IOException {
        Move playerMove = player.move(provider, this);
        RoguelikeLogger.INSTANCE.log_info("Move " + playerMove);
        applyMove(player, playerMove);

        mobs = mobs.stream().filter(AbstractGameParticipant::isAlive).collect(Collectors.toList());

        for (AbstractGameParticipant mob : mobs) {
            Move to = mob.move(provider, this);
            RoguelikeLogger.INSTANCE.log_info("Mob move " + to + " from position " + mob.getPosition().getX()
                    + " " + mob.getPosition().getY());
            applyMove(mob, to);
        }

        if (!player.isAlive()) {
            RoguelikeLogger.INSTANCE.log_info("Lose");
            if (isSavedGameEqualToCurrent && isFinished) {
                GameSaverAndLoader saverAndLoader = new GameSaverAndLoader();
                //saverAndLoader.deleteGame();
            }
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
            case UP:
                if (isValidPosition(pos.up())) {
                    to = pos.up();
                }
                break;
            case DOWN:
                if (isValidPosition(pos.bottom())) {
                    to = pos.bottom();
                }
                break;
        }
        if (pos.equals(to)) {
            return;
        }

        for (AbstractGameParticipant opponent : mobs) {
            if (to.equals(opponent.getPosition())) {
                attack(participant, opponent);
                if (opponent.isAlive()) {
                    return;
                }
            }
        }

        if (participant instanceof Player) {
            for (Artifact artifact : artifacts) {
                if (to.equals(artifact.getPosition()) &&
                        !artifact.taken()) {
                    player.addArtifact(artifact);
                    artifact.take();
                    break;
                }
            }

            if (to.equals(key.getPosition())) {
                player.addArtifact(key);
                isFinished = true;
            }
        }


        if (to.equals(player.getPosition())) {
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
        return position.getX() >= 0 &&
                position.getY() >= 0 &&
                position.getX() < fieldModel.size() &&
                position.getY() < fieldModel.get(0).size();
    }

    private void attack(AbstractGameParticipant attacker, AbstractGameParticipant defender) {
        if ((attacker instanceof Player && defender instanceof Mob) ||
                (defender instanceof Player && attacker instanceof Mob)) {
            attacker.hit(defender);
        }
    }

    /**
     * Returns flag of showing help screen
     * @return show help flag
     */
    public boolean isShowHelpScreen() {
        return showHelpScreen;
    }

    /**
     * Sets flag of showing help screen
     */
    public void setShowHelpScreen(boolean showHelpScreen) {
        this.showHelpScreen = showHelpScreen;
    }

    /**
     * Returns flag of loading map from file
     * @return loading map from file flag
     */
    public boolean isLoadMapFromFile() {
        return loadMapFromFile;
    }

    /**
     * Sets flag of loading map from file
     */
    public void setLoadMapFromFile(boolean loadMapFromFile) {
        this.loadMapFromFile = loadMapFromFile;
    }

    /**
     * Returns flag of error loading map from file
     * @return loading map from file error flag
     */
    public boolean isErrorWhileLoadingMap() {
        return errorWhileLoadingMap;
    }

    /**
     * Sets error flag of loading map from file
     */
    public void setErrorWhileLoadingMap(boolean errorWhileLoadingMap) {
        this.errorWhileLoadingMap = errorWhileLoadingMap;
    }

    @Override
    public String[] getStartMenuOptions() {
        return startMenuOptions;
    }

    public Player getPlayer() {
        return player;
    }

    public FinalKey getKey() {
        return key;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public List<AbstractGameParticipant> getMobs() {
        return mobs;
    }

    public boolean isSavedGameEqualToCurrent() {
        return isSavedGameEqualToCurrent;
    }

    public void setSavedGameEqualToCurrent(boolean savedGameEqualToCurrent) {
        isSavedGameEqualToCurrent = savedGameEqualToCurrent;
    }
}
