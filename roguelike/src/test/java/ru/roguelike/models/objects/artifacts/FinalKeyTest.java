package ru.roguelike.models.objects.artifacts;

import org.junit.Test;
import ru.roguelike.logic.GameModel;
import ru.roguelike.logic.GameModelImpl;
import ru.roguelike.logic.commands.ApplyMoveCommand;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameObject;
import ru.roguelike.models.objects.movable.Player;
import ru.roguelike.view.StringStreamInputProviderImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing final key mechanics
 */
public class FinalKeyTest {
    /**
     * Testing final key taking
     * @throws IOException
     */
    @Test
    public void testFinalKey() throws IOException {
        FinalKey key = new FinalKey(new Position(0, 0));
        Player player = new Player(new Position(0, 1));

        List<List<AbstractGameObject>> field = new ArrayList<>();
        field.add(new ArrayList<>());
        field.get(0).add(key);
        field.get(0).add(player);

        GameModel model = new GameModelImpl(field, player, key, new ArrayList<>(), new ArrayList<>());

        //game not finished
        assertFalse(model.finished());

        //finish game
        model.makeMove(ApplyMoveCommand.applyPlayerAction(new StringStreamInputProviderImpl("a"), model));

        assertTrue(model.finished());
    }
}