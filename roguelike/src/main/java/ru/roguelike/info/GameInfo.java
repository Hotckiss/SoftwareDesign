package ru.roguelike.info;

import java.util.ArrayList;
import java.util.List;

public class GameInfo {
    private static List<String> getMobsInfo() {
        List<String> info = new ArrayList<>();
        info.add("S - Simple Mob. Able to do physical damage.");
        info.add("    Have less health than a player. Does not have baseline regeneration.");
        info.add("B - Berserk. A mob that has increased physical damage,");
        info.add("    but less health than a regular monster.");
        info.add("V - Vampire. It differs from the normal monster in that");
        info.add("    it has a smaller number of health, but when it deals");
        info.add("    physical damage, it recovers a fraction of that damage.");
        info.add("F - Flier. An ordinary monster with less health and capable");
        info.add("    of delivering two hits per turn.");
        info.add("M - Magician. A monster with a small amount of health that can,");
        info.add("    however, regenerate. It has low physical damage,");
        info.add("    but always deals fire damage.");
        info.add("Y - Yeti. An ordinary monster with low physical damage, which,");
        info.add("    however, has a chance to cause damage by cold.");

        return info;
    }

    private static List<String> getControlInfo() {
        List<String> info = new ArrayList<>();
        info.add("w, a, s, d - move top, left, down, right respectively.");
        info.add("e - equip the player with the latest artifact.");
        info.add("q - remove the artifact from the player.");

        return info;
    }

    public static List<String> getInfo() {
        List<String> info = new ArrayList<>();

        info.add("Game Info:         (r - return to the game)");
        info.add("");

        info.add("Mobs:");
        info.addAll(getMobsInfo());
        info.add("");

        info.add("Control:");
        info.addAll(getControlInfo());

        return info;
    }
}
