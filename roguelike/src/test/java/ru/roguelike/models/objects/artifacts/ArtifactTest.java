package ru.roguelike.models.objects.artifacts;

import org.junit.Test;
import ru.roguelike.logic.generators.GenerationUtils;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.movable.Player;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing artifacts
 */
public class ArtifactTest {
    /**
     * Testing health water
     */
    @Test
    public void testHealthWater() {
        Artifact artifact = GenerationUtils.makeArtifact('h', new Position(0, 0));
        //health=100
        Player player = new Player(new Position(0, 1));
        AbstractGameParticipant mob = GenerationUtils.makeMob('B', new Position(0 ,2));
        //health=20
        mob.hit(player);
        mob.hit(player);

        //health=70
        player.addArtifact(artifact);
        player.enableArtifact(artifact);

        assertEquals(70, player.getHealth());
    }

    /**
     * Testing regeneration potion
     */
    @Test
    public void testRegenerationPotion() {
        Artifact artifact = GenerationUtils.makeArtifact('r', new Position(0, 0));
        //health=100
        Player player = new Player(new Position(0, 1));
        AbstractGameParticipant mob = GenerationUtils.makeMob('B', new Position(0 ,2));
        //health=20
        mob.hit(player);
        mob.hit(player);

        //health=50
        player.addArtifact(artifact);
        player.enableArtifact(artifact);

        //health=60
        player.regenerate();
        assertEquals(60, player.getHealth());
    }

    /**
     * Test applying artifact
     */
    @Test
    public void testEnableArtifact() {
        Artifact artifact = GenerationUtils.makeArtifact('r', new Position(0, 0));
        //health=100
        Player player = new Player(new Position(0, 1));
        assertEquals(100, player.getHealth());
        AbstractGameParticipant mob = GenerationUtils.makeMob('B', new Position(0 ,2));
        //health=20
        mob.hit(player);
        mob.hit(player);
        assertEquals(20, player.getHealth());
        //health=25
        player.regenerate();
        assertEquals(25, player.getHealth());
        //health=55
        player.addArtifact(artifact);
        player.enableArtifact(artifact);
        assertEquals(55, player.getHealth());
        //health=65
        player.regenerate();
        assertEquals(65, player.getHealth());
    }

    /**
     * Test disabling artifact
     */
    @Test
    public void testDisableArtifact() {
        Artifact artifact = GenerationUtils.makeArtifact('r', new Position(0, 0));
        //health=100
        Player player = new Player(new Position(0, 1));
        assertEquals(100, player.getHealth());
        AbstractGameParticipant mob = GenerationUtils.makeMob('B', new Position(0 ,2));
        //health=20
        mob.hit(player);
        mob.hit(player);
        assertEquals(20, player.getHealth());
        //health=25
        player.regenerate();
        assertEquals(25, player.getHealth());
        //health=55
        player.addArtifact(artifact);
        player.enableArtifact(artifact);
        assertEquals(55, player.getHealth());
        //health=65
        player.regenerate();
        assertEquals(65, player.getHealth());

        //health=70
        player.disableArtifact(artifact);
        player.regenerate();
        assertEquals(70, player.getHealth());
    }
}