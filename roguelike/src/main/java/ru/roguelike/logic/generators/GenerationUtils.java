package ru.roguelike.logic.generators;

import org.jetbrains.annotations.NotNull;
import ru.roguelike.logic.strategies.implementations.AggressiveStrategy;
import ru.roguelike.logic.strategies.implementations.CowardStrategy;
import ru.roguelike.logic.strategies.implementations.PassiveStrategy;
import ru.roguelike.logic.strategies.implementations.RandomStrategy;
import ru.roguelike.models.Position;
import ru.roguelike.models.objects.artifacts.Artifact;
import ru.roguelike.models.objects.base.AbstractGameParticipant;
import ru.roguelike.models.objects.movable.Mob;

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
                return new Mob(position,
                        30,
                        40,
                        0,
                        0,
                        0,
                        1,
                        1,
                        0,
                        4,
                        30,
                        new AggressiveStrategy(),
                        alias);
            case 'F':
                return new Mob(position,
                        10,
                        20,
                        0,
                        0,
                        0,
                        1,
                        1,
                        0,
                        2,
                        7,
                        new CowardStrategy(),
                        alias);
            case 'M' :
                return new Mob(position,
                        20,
                        40,
                        0,
                        0,
                        1,
                        1,
                        1,
                        15,
                        6,
                        30,
                        new AggressiveStrategy(),
                        alias);
            case 'S':
                return new Mob(position,
                        50,
                        20,
                        0,
                        0,
                        0,
                        1,
                        1,
                        0,
                        3,
                        18,
                        new RandomStrategy(),
                        alias);
            case 'V':
                return new Mob(position,
                        40,
                        10,
                        0,
                        0,
                        0,
                        1,
                        1,
                        10,
                        1,
                        10,
                        new PassiveStrategy(),
                        alias);
            case 'Y':
                return new Mob(position,
                        50,
                        15,
                        0,
                        0.4,
                        0,
                        1,
                        1,
                        10,
                        6,
                        25,
                        new AggressiveStrategy(),
                        alias);
            default:
                return new Mob(position,
                        50,
                        20,
                        0,
                        0,
                        0,
                        1,
                        1,
                        0,
                        3,
                        18,
                        new RandomStrategy(),
                        alias);
        }
    }
}
