package ru.roguelike.logic.strategies.implementations;

import org.junit.Test;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.GameModelImpl;
import ru.roguelike.logic.commands.ApplyMoveCommand;
import ru.roguelike.logic.generators.GenerationUtils;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.FinalKey;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.map.FreePlace;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.StringStreamInputProviderImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Check correctness of coward strategy
 */
public class CowardStrategyTest {
    /**
     * Test correctness of move
     * @throws IOException
     */
    @Test
    public void preferredMove() throws IOException {
        FinalKey key = new FinalKey(new Position(0, 3));
        Player player = new Player(new Position(0, 0));
        AbstractGameParticipant mob = GenerationUtils.makeMob('F', new Position(0 ,1));

        List<List<AbstractGameObject>> field = new ArrayList<>();
        field.add(new ArrayList<>());
        field.get(0).add(player);
        field.get(0).add(mob);
        field.get(0).add(new FreePlace(new Position(0, 2)));
        field.get(0).add(key);

        GameModel model = new GameModelImpl(field, player, key, Collections.singletonList(mob), new ArrayList<>());

        model.makeMove(ApplyMoveCommand.applyPlayerAction(new StringStreamInputProviderImpl("a"), model));
        //mob moved right from player (0, 1) -> (0, 2)
        assertEquals(new Position(0, 2), mob.getPosition());
    }
}