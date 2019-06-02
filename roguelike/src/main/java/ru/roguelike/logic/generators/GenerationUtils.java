package ru.roguelike.logic.generators;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.Artifact;
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
                return new Artifact(position,
                        true,
                        0,
                        0,
                        0,
                        0.2,
                        0,
                        0,
                        alias);
            case 'g':
                return new Artifact(position,
                        true,
                        0,
                        0,
                        0,
                        0,
                        0,
                        1.0,
                        alias);
            case 'f':
                return new Artifact(position,
                        true,
                        0,
                        0,
                        0.25,
                        0,
                        0,
                        0,
                        alias);
            case 'h':
                return new Artifact(position,
                        true,
                        50,
                        0,
                        0,
                        0,
                        0,
                        0,
                        alias);
            case 'r':
                return new Artifact(position,
                        true,
                        30,
                        5,
                        0,
                        0,
                        0,
                        0,
                        alias);
            case 's':
                return new Artifact(position,
                        true,
                        30,
                        5,
                        0,
                        0,
                        0,
                        0,
                        alias);
            default:
                return new Artifact(position,
                        true,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        alias);
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
