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
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 */
public class GameModelImpl implements GameModel {
    private List<List<AbstractGameObject>> fieldModel;
    // all players are stored in map
    private Map<Integer, Player> players;
    private Integer activePlayerIndex = 0;
    private FinalKey key;
    private List<AbstractGameParticipant> mobs;
    private List<Artifact> artifacts;
    private boolean isFinished = false;

    public GameModelImpl(List<List<AbstractGameObject>> fieldModel,
                         Player player,
                         FinalKey key,
                         List<AbstractGameParticipant> mobs,
                         List<Artifact> artifacts) {
        Map<Integer, Player> players = new HashMap<>();
        players.put(0, player);

        this.fieldModel = fieldModel;
        this.players = players;
        this.key = key;
        this.mobs = mobs;
        this.artifacts = artifacts;
    }

    public GameModelImpl(List<List<AbstractGameObject>> fieldModel,
                         Map<Integer, Player> players,
                         FinalKey key,
                         List<AbstractGameParticipant> mobs,
                         List<Artifact> artifacts) {
        this.fieldModel = fieldModel;
        this.players = players;
        this.key = key;
        this.mobs = mobs;
        this.artifacts = artifacts;
    }

    /**
     * Add random player to game. Returns id
     * @return player id
     */
    public Integer addPlayerRandom() {
        Integer id = generateId();
        List<Position> available = new ArrayList<>();

        for (int i = 0; i < fieldModel.size(); i++) {
            for (int j = 0; j < fieldModel.get(i).size(); j++) {
                if (fieldModel.get(i).get(j) instanceof FreePlace) {
                    available.add(fieldModel.get(i).get(j).getPosition());
                }
            }
        }

        Position position = available.get(new Random().nextInt(available.size()));
        Player newPlayer = new Player(position);

        players.put(id, newPlayer);
        fieldModel.get(position.getX()).set(position.getY(), newPlayer);

        return id;
    }

    /**
     * Add player to game. Returns id
     * @param newPlayer player to add
     * @return player id
     */
    public Integer addPlayer(@NotNull Player newPlayer) {
        Integer id = generateId();

        players.put(id, newPlayer);
        fieldModel.get(newPlayer.getPosition().getX()).set(newPlayer.getPosition().getY(), newPlayer);

        return id;
    }

    private Integer generateId() {
        Integer id = 0;
        try {
            id = Collections.max(players.keySet()) + 1;
        } catch (NoSuchElementException e) {
            RoguelikeLogger.INSTANCE.log_info(e.getMessage());
        }

        return id;
    }

    public void nextActivePlayer() {
        if (players.keySet().isEmpty()) {
            activePlayerIndex = 0;
        }
        activePlayerIndex = (activePlayerIndex + 1) % players.keySet().size();
    }

    public void removePlayer(Integer id) {
        players.remove(id);
        nextActivePlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeMove(@NotNull UserInputProvider provider) throws IOException {
        Player currentPlayer = getPlayer();
        Move playerMove = currentPlayer.move(provider, this);
        RoguelikeLogger.INSTANCE.log_info("Move " + playerMove);
        applyMove(currentPlayer, playerMove);

        //remove burned mobs
        mobs = mobs.stream().filter(AbstractGameParticipant::isAlive).collect(Collectors.toList());

        // mobs move each round
        if (activePlayerIndex == players.keySet().size() - 1) {
            for (AbstractGameParticipant mob : mobs) {
                Move to = mob.move(provider, this);
                RoguelikeLogger.INSTANCE.log_info("Mob move " + to + " from position " + mob.getPosition().getX()
                        + " " + mob.getPosition().getY());
                applyMove(mob, to);
            }

            for (AbstractGameParticipant mob : mobs) {
                mob.fireStep();
                mob.regenerate();
                mob.freezeStep();
            }
        }

        if (!currentPlayer.isAlive()) {
            RoguelikeLogger.INSTANCE.log_info("Lose");
        }

        //remove burned mobs
        mobs = mobs.stream().filter(AbstractGameParticipant::isAlive).collect(Collectors.toList());

        currentPlayer.fireStep();

        if (currentPlayer.isAlive()) {
            currentPlayer.regenerate();
            currentPlayer.freezeStep();
        } else {
            isFinished = isAllPlayersDied();
        }

        nextActivePlayer();
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

        Player currentPlayer = getPlayer();
        if (participant instanceof Player) {
            for (Artifact artifact : artifacts) {
                if (to.equals(artifact.getPosition()) &&
                        !artifact.taken()) {
                    currentPlayer.addArtifact(artifact);
                    artifact.take();
                    break;
                }
            }

            if (to.equals(key.getPosition())) {
                currentPlayer.addArtifact(key);
                isFinished = true;
            }
        }

        for (Player player: players.values()) {
            if (to.equals(player.getPosition())) {
                attack(participant, player);
                if (player.isAlive()) {
                    return;
                } else {
                    isFinished = isAllPlayersDied();
                }
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

    public Player getPlayer() {
        return players.get(new ArrayList<>(players.keySet()).get(activePlayerIndex));
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getLog() {
        List<String> gameSituation = new ArrayList<>();
        Player currentPlayer = getPlayer();

        gameSituation.add("Health: " + currentPlayer.getHealth() +
                " Exp: " + currentPlayer.exp() + " Level: " +
                currentPlayer.getLevel() + " Items: " + currentPlayer.getArtifactsLog());

        StringBuilder mobsHealth = new StringBuilder();
        mobsHealth.append("Mobs' health: ");

        for (AbstractGameParticipant mob : mobs) {
            mobsHealth.append(mob.getHealth());
            mobsHealth.append(" ");
        }

        gameSituation.add(mobsHealth.toString());

        if (!currentPlayer.isAlive() && isFinished) {
            gameSituation.add("You lose!");
        }

        if (currentPlayer.isAlive() && isFinished) {
            gameSituation.add("You win!");
        }
        RoguelikeLogger.INSTANCE.log_info(String.join(System.lineSeparator(), gameSituation));

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

    private boolean isAllPlayersDied() {
        for (Map.Entry<Integer, Player> playerEntry: players.entrySet()) {
            if (playerEntry.getValue().isAlive()) {
                return false;
            }
        }

        return true;
    }
}
