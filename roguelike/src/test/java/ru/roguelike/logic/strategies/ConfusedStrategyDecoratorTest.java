package ru.roguelike.logic.strategies;

import org.junit.Test;
import ru.roguelike.logic.generators.GenerationUtils;
import ru.roguelike.logic.strategies.implementations.AggressiveStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.movable.Mob;
import ru.roguelike.models.objects.movable.Player;

import static org.junit.Assert.*;

public class ConfusedStrategyDecoratorTest {

    /**
     * Test correctness of confusing
     */
    @Test
    public void preferredMove() {
        Player player = new Player(new Position(0, 0));
        AbstractGameParticipant mob = GenerationUtils.makeMob('B', new Position(0 ,1));

        assertTrue(mob instanceof Mob);

        assertTrue(((Mob)mob).getMobStrategy() instanceof AggressiveStrategy);

        player.hit(mob);

        assertTrue(((Mob)mob).getMobStrategy() instanceof ConfusedStrategyDecorator);
    }
}