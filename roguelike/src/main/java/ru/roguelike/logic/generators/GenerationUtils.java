package ru.roguelike.logic.generators;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.*;
import ru.roguelike.models.objects.base.Artifact;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.movable.*;

/**
 * Common utils for objects generation
 */
public class GenerationUtils {
    /**
     * Generates artifact by id
     * @param alias artifact id
     * @param position artifact position
     * @return artifact
     */
    @NotNull
    public static Artifact makeArtifact(@NotNull Character alias, Position position) {
        switch (alias) {
            case 'c':
                return new ColdSword(position);
            case 'g':
                return new FireGoblet(position);
            case 'f':
                return new FireSword(position);
            case 'h':
                return new HealthWater(position);
            case 'r':
                return new RegenerationPotion(position);
            case 's':
                return new SpeedBoots(position);
            default:
                return new HealthWater(position);
        }
    }

    /**
     * Generates mob by id
     * @param alias mob id
     * @param position mob position
     * @return mob
     */
    @NotNull
    public static AbstractGameParticipant makeMob(@NotNull Character alias, Position position) {
        switch (alias) {
            case 'B':
                return new Berserk(position);
            case 'F':
                return new Flier(position);
            case 'M' :
                return new Magician(position);
            case 'S':
                return new SimpleMob(position);
            case 'V':
                return new Vampire(position);
            case 'Y':
                return new Yeti(position);
            default:
                return new SimpleMob(position);
        }
    }
}
