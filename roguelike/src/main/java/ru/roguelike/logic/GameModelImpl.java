package ru.roguelike.logic;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.RoguelikeLogger;
import ru.roguelike.logic.generators.GenerationUtils;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.DistancedPosition;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.map.Wall;
import ru.roguelike.models.objects.movable.Mob;
import ru.roguelike.models.objects.movable.Player;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
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

    /**
     * Creates new game model
     * @param fieldModel game field
     * @param player first game player
     * @param key final key to win
     * @param mobs list of mobs
     * @param artifacts list of artifacts
     */
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

    /**
     * Add random player to game. Returns id
     * @return player id
     */
    @Override
    public Integer addPlayerRandom() {
        Integer id = generateId();
        Predicate<AbstractGameObject> wallTest = abstractGameObject -> !(abstractGameObject instanceof Wall);

        List<DistancedPosition> available =
                GenerationUtils.connectedPositions(fieldModel, key.getPosition(), wallTest,false, true);

        Position position = available.get(new Random().nextInt(available.size()));
        Player newPlayer = new Player(position);

        players.put(id, newPlayer);
        fieldModel.get(position.getX()).set(position.getY(), newPlayer);

        return id;
    }

    /**
     * Move turn to next player method
     */
    @Override
    public void nextActivePlayer() {
        if (players.keySet().isEmpty()) {
            isFinished = true;
            activePlayerIndex = 0;
            return;
        }

        activePlayerIndex = (activePlayerIndex + 1) % players.keySet().size();
    }

    /**
     * Method to remove player that left game
     * @param id player to remove
     */
    @Override
    public void removePlayer(Integer id) {
        Player removedPlayer = players.get(id);
        if (removedPlayer == null) {
            return;
        }

        fieldModel.get(removedPlayer.getPosition().getX()).set(removedPlayer.getPosition().getY(), new FreePlace(removedPlayer.getPosition()));
        players.remove(id);
        nextActivePlayer();
    }

    /**
     * Get player making turn
     * @return active player
     */
    @Override
    public Player getActivePlayer() {
        return players.get(getActivePlayerId());
    }

    /**
     * Get player by ID
     * @return player with this id or null of player does not exist
     */
    @Override
    public Player getPlayerById(Integer id) {
        return players.get(id);
    }

    /**
     * Get ID of player making turn
     * @return active player id
     */
    @Override
    public Integer getActivePlayerId() {
        return new ArrayList<>(players.keySet()).get(activePlayerIndex);
    }

    /**
     * Get all players
     * @return all players
     */
    @Override
    public List<Player> getAllPlayers() {
        return new ArrayList<>(players.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeMove(Move playerMove) throws IOException {
        if (isFinished) {
            return;
        }

        Player currentPlayer = getActivePlayer();
        RoguelikeLogger.INSTANCE.log_info("Move " + playerMove);
        applyMove(currentPlayer, playerMove);

        //remove burned mobs
        mobs = mobs.stream().filter(AbstractGameParticipant::isAlive).collect(Collectors.toList());

        // mobs move each round
        if (activePlayerIndex == players.keySet().size() - 1) {
            for (AbstractGameParticipant mob : mobs) {
                Move to = mob.move(this);
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

    private Integer generateId() {
        int id = 0;
        try {
            id = Collections.max(players.keySet()) + 1;
        } catch (NoSuchElementException e) {
            RoguelikeLogger.INSTANCE.log_info(e.getMessage());
        }

        return id;
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

        Player currentPlayer = getActivePlayer();
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

    @Override
    public FinalKey getKey() {
        return key;
    }

    @Override
    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    @Override
    public List<AbstractGameParticipant> getMobs() {
        return mobs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getLog(Integer playerId) {
        List<String> gameSituation = new ArrayList<>();
        Player currentPlayer = getPlayerById(playerId);

        gameSituation.add("Health: " + currentPlayer.getHealth() +
                " Exp: " + currentPlayer.exp() + " Level: " +
                currentPlayer.getLevel() + " Items: " + currentPlayer.getArtifactsLog(true));

        StringBuilder mobsHealth = new StringBuilder();
        mobsHealth.append("Mobs' health: ");

        for (AbstractGameParticipant mob : mobs) {
            mobsHealth.append(mob.getHealth());
            mobsHealth.append(" ");
        }

        gameSituation.add(mobsHealth.toString());

        if (isFinished) {
            boolean hasKey = false;
            for (Player.ArtifactInInventory artifact: currentPlayer.getArtifacts()) {
                if (artifact.getItem() instanceof FinalKey) {
                    hasKey = true;
                }
            }

            if (currentPlayer.isAlive() && hasKey) {
                gameSituation.add("You win!");
            } else {
                gameSituation.add("You lose!");
            }
        } else {
            if (!currentPlayer.isAlive()) {
                gameSituation.add("You died!");
            }
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
        info.add("Press l for loading map from file, v for saving game(not for multiplayer)");
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
