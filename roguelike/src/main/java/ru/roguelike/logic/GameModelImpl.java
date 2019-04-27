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

    @Override
    public List<AbstractGameParticipant> getMobs() {
        mobs = mobs.stream().filter(AbstractGameParticipant::isAlive).collect(Collectors.toList());
        return mobs;
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

    public boolean isValidPosition(@NotNull Position position) {
        return position.getX() >= 0 && position.getY() >= 0 && position.getX() < fieldModel.size() && position.getY() < fieldModel.get(0).size();
    }
}
