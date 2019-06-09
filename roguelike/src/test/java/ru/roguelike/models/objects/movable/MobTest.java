package ru.roguelike.models.objects.movable;

import org.junit.Test;
import ru.roguelike.logic.generators.GenerationUtils;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameParticipant;

import static org.junit.Assert.*;

/**
 * Class for test mob properties
 */
public class MobTest {
    /**
     * Testing berserk
     */
    @Test
    public void testBerserk() {
        //health=100
        Player player = new Player(new Position(0, 1));
        AbstractGameParticipant mob = GenerationUtils.makeMob('B', new Position(0 ,2));
        //health=20
        mob.hit(player);
        mob.hit(player);

        assertEquals(20, player.getHealth());
    }

    /**
     * Testing simple mob
     */
    @Test
    public void testSimpleMob() {
        //health=100
        Player player = new Player(new Position(0, 1));
        AbstractGameParticipant mob = GenerationUtils.makeMob('S', new Position(0 ,2));
        //health=60
        mob.hit(player);
        mob.hit(player);

        assertEquals(60, player.getHealth());
    }

    /**
     * Testing fire damage
     */
    @Test
    public void testMagician() {
        //health=100
        Player player = new Player(new Position(0, 1));
        AbstractGameParticipant mob = GenerationUtils.makeMob('M', new Position(0 ,2));
        //health=60
        mob.hit(player);

        assertEquals(60, player.getHealth());

        //health=55
        player.fireStep();
        assertEquals(55, player.getHealth());

        //health=50
        player.fireStep();
        assertEquals(50, player.getHealth());

        //health=45
        player.fireStep();
        assertEquals(45, player.getHealth());

        //health=45
        player.fireStep();
        assertEquals(45, player.getHealth());
    }
}